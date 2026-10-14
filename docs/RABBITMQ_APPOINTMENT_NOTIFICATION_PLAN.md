# RabbitMQ 预约通知学习与接入计划

## 1. 目标

在不拆分微服务、不接入真实短信或邮件的前提下，为当前租赁项目实现以下业务链路：

> 用户预约看房 → 保存预约 → 发布预约创建事件 → 消费者生成一条站内通知

本练习的重点不是单纯完成 RabbitMQ 的消息收发，而是掌握消息系统中的三个核心问题：

1. 消息没有成功发布时如何发现和补偿；
2. 消费者处理失败时如何有限重试并进入死信流程；
3. 消息重复到达时如何保证业务结果不重复。

预约是否成功仍由核心业务和数据库事务决定。RabbitMQ 只负责异步触发通知，不应改变预约接口本身的业务成功语义。

## 2. 为什么适合当前项目

当前 `web-app` 已有完整的预约入口：

- Controller：`web/web-app/src/main/java/com/rz/lease/web/app/controller/appointment/ViewAppointmentController.java`
- Service：`web/web-app/src/main/java/com/rz/lease/web/app/service/impl/ViewAppointmentServiceImpl.java`
- Entity：`model/src/main/java/com/rz/lease/model/entity/ViewAppointment.java`
- Repository：`web/web-app/src/main/java/com/rz/lease/web/app/repository/ViewAppointmentRepository.java`

其中 `ViewAppointmentServiceImpl.saveOrUpdate(...)` 已使用数据库事务，可以在现有单体应用中自然地加入领域事件、RabbitMQ 发布和通知消费，无需为了学习消息队列提前拆分微服务。

如果目标仅仅是实现一个站内通知功能，使用数据库任务也可以完成；这里引入 RabbitMQ 的主要价值是练习异步解耦与可靠消息处理。

## 3. 必须掌握的概念

### 3.1 Publisher Confirm 不等于业务处理成功

需要区分三个不同阶段：

1. **Publisher Confirm/Nack**：RabbitMQ Broker 是否接管了发布的消息；
2. **Returned Message**：消息是否因为没有匹配的队列而无法路由；
3. **Consumer Acknowledgement**：消费者是否已经完成数据库写入等业务处理。

Publisher Confirm 与 Consumer Acknowledgement 相互独立。发布端收到确认，不代表消费者已经生成通知。

此外，无法路由的消息也可能收到 Publisher Confirm，因此发布端还应结合 `mandatory` 和 returned-message 处理来判断消息是否进入了目标路由。

### 3.2 可靠投递通常意味着消息可能重复

消费者崩溃、连接中断或者业务写库成功但 acknowledgement 丢失时，同一条消息可能再次投递。因此消费者必须是幂等的，不能把“开启重试”当作完整的可靠性方案。

### 3.3 Spring AUTO acknowledgement 不等于 RabbitMQ autoAck

Spring AMQP 的 `AcknowledgeMode.AUTO` 会在监听方法成功结束后确认消息；监听方法抛出异常时不会按成功处理。它不同于 RabbitMQ 协议中消息一经发送便认为成功的 `autoAck=true`。

第一轮可以使用 Spring 的 AUTO 模式，不必一开始就手写 `basicAck`，但必须明确 acknowledgement 发生在通知业务成功之后。

## 4. 第一轮目标架构

```text
预约数据库事务
    │
    ├─ 保存 view_appointment
    │
    └─ 事务提交成功
           │
           ▼
发布 appointment.created
           │
           ▼
lease.appointment.events（Exchange）
           │ routing key: appointment.created
           ▼
lease.notification.appointment-created.v1（Queue）
           │
           ▼
通知消费者
           │
           ▼
user_notification（UNIQUE event_id）

消费持续失败并耗尽重试
           │
           ▼
lease.dlx（Dead Letter Exchange）
           │
           ▼
lease.notification.appointment-created.dlq
```

生产者和消费者第一轮都保留在 `web-app` 中。它们是同一个部署单元中的不同组件，不需要新建微服务。

## 5. 事件设计

建议定义独立的事件 DTO，例如 `AppointmentCreatedEvent`，不要直接序列化 JPA 实体。

最小字段：

| 字段 | 用途 |
| --- | --- |
| `eventId` | 全局唯一事件标识，也是消费者幂等键 |
| `eventType` | 事件类型，例如 `appointment.created` |
| `eventVersion` | 事件契约版本，第一版可设为 `1` |
| `occurredAt` | 事件发生时间 |
| `appointmentId` | 预约主键 |
| `userId` | 接收通知的用户 |
| `apartmentId` | 对应公寓 |
| `appointmentTime` | 预约看房时间 |

事件中不应包含通知消费者不需要的手机号等敏感信息。

重复消息实验必须重复发送**同一个 `eventId`**。如果每次重新生成 UUID，业务上会被视为两个不同事件，不能验证幂等性。

## 6. 预约创建与事件发布边界

当前预约接口同时承担 `saveOrUpdate`。实现事件时必须区分新增和更新：

- 只有新建预约才发布 `appointment.created`；
- 修改预约时间不应再次发布创建事件；
- 取消预约、完成看房以后可以分别扩展为独立事件，但不在第一轮范围内。

推荐的第一轮调用方式：

```text
saveOrUpdate/createAppointment
    → 保存预约
    → 发布 Spring 应用内领域事件
    → 数据库事务提交
    → @TransactionalEventListener(AFTER_COMMIT)
    → RabbitTemplate 发布 RabbitMQ 消息
```

选择 `AFTER_COMMIT` 是为了避免 RabbitMQ 已收到事件、预约数据库事务却随后回滚的情况。

如果事务提交后 RabbitMQ 发布失败：

- 预约仍然成功；
- 记录清晰的失败日志和 `eventId`；
- 第一轮允许人工观察这个一致性缺口；
- 第二轮通过 Outbox 实现自动补发。

不能用 Publisher Confirm 解决数据库和 RabbitMQ 之间的双写原子性问题。

## 7. 通知表与消费者幂等

第一轮增加 `user_notification` 表，建议至少包含：

| 字段 | 说明 |
| --- | --- |
| `id` | 主键 |
| `event_id` | 来源事件 ID |
| `user_id` | 用户 ID |
| `notification_type` | 通知类型 |
| `title` | 通知标题 |
| `content` | 通知内容 |
| `is_read` | 是否已读 |
| `create_time` | 创建时间 |
| `update_time` | 更新时间 |
| `is_deleted` | 逻辑删除标识 |

数据库必须提供唯一约束，例如：

```sql
UNIQUE KEY uk_user_notification_event_id (event_id)
```

消费者处理流程：

```text
收到事件
    → 校验事件版本和必要字段
    → 在数据库事务中插入通知
    → 插入成功后结束监听方法
    → Spring 向 RabbitMQ acknowledgement
```

如果唯一键冲突，说明相同事件已经产生通知，应将其视为幂等成功并正常结束消费。

不能只使用“先查询、查不到再插入”的方式实现幂等，因为并发消费者可能同时通过查询。数据库唯一约束才是最终防线。

## 8. 重试与死信策略

第一轮采用易于观察的策略：

- 最大尝试次数：3 次；
- 使用退避，例如 1 秒、2 秒、4 秒；
- 重试耗尽后 reject 且不重新进入原队列；
- 由 RabbitMQ 路由至明确配置的 DLX 和 DLQ；
- 暂不自动消费 DLQ，方便通过 Management UI 检查失败消息；
- 日志记录 `eventId`、当前尝试次数和异常原因。

应明确配置 Spring AMQP 的重试 recoverer/拒绝行为，确保耗尽后为 `reject-and-don't-requeue`。不能让异常消息在原队列中无限重新入队。

DLQ 不是最终处理方案。后续可以增加告警、失败原因查看和人工重放，但不属于第一轮范围。

RabbitMQ 官方建议优先通过 policy 配置 DLX，因为 policy 可以动态调整；硬编码的队列 `x-arguments` 往往需要删除并重新声明队列才能修改。学习阶段可以先在代码中声明完整拓扑，但应理解两种方式的运维差异。

## 9. 持久性与拓扑建议

第一轮至少保证：

- Exchange durable；
- Queue durable；
- DLX 和 DLQ durable；
- 业务消息使用 persistent delivery mode；
- RabbitMQ Docker 镜像固定到明确版本，不使用浮动的 `latest`；
- 开启 Management UI，观察 ready、unacked、publish、deliver 和 dead-letter 状态。

建议命名：

| 类型 | 名称 |
| --- | --- |
| Exchange | `lease.appointment.events` |
| Routing key | `appointment.created` |
| Queue | `lease.notification.appointment-created.v1` |
| DLX | `lease.dlx` |
| Dead-letter routing key | `notification.appointment-created.failed` |
| DLQ | `lease.notification.appointment-created.dlq` |

## 10. 当前仓库中的预计改动位置

### `web-app`

- 在 `web/web-app/build.gradle` 增加 `spring-boot-starter-amqp`；
- 在配置文件增加 `spring.rabbitmq.*` 连接、Publisher Confirm/Return 和 listener retry 配置；
- 增加 RabbitMQ Exchange、Queue、Binding 和消息转换配置；
- 增加预约事件 DTO、事件发布器和 RabbitMQ producer；
- 增加通知 Repository、Service 和 `@RabbitListener` consumer；
- 增加控制监听器启动的配置开关，方便执行停止/恢复消费者实验。

### `model`

- 增加 `UserNotification` 实体；
- 如有需要，增加通知类型枚举。

### 数据库

- 在 `sql_scripts/lease.sql` 增加通知表及 `event_id` 唯一索引；
- 第二轮增加 `outbox_event` 表。

### Docker Compose

- 为本地开发环境增加 RabbitMQ 服务；
- 暴露 AMQP 端口 `5672` 和仅供本机使用的 Management UI 端口 `15672`；
- 配置数据卷与 healthcheck；
- 不要求第一轮将 producer 和 consumer 拆成两个服务。

## 11. 四个核心实验

### 实验一：停止消费者，再启动

目的：验证消费者暂时不可用时，消息仍会在队列中等待。

步骤：

1. 保持 RabbitMQ 运行，停止或禁用通知 listener；
2. 创建一条预约；
3. 在 Management UI 确认主队列 `ready` 数量增加；
4. 重新启动 listener；
5. 确认消息被消费，通知表新增一条记录，队列恢复为空。

补充实验：重启 RabbitMQ Broker，验证 durable queue 与 persistent message 能够使尚未消费的消息在 Broker 重启后保留。

### 实验二：重复发送同一事件

目的：验证 at-least-once 投递下的消费者幂等性。

步骤：

1. 固定一个 `eventId`；
2. 连续发布两次或更多次内容相同的事件；
3. 确认消费者收到多次投递；
4. 查询通知表，确认该 `eventId` 最终只有一条记录；
5. 确认后续重复事件没有进入无限重试或 DLQ。

### 实验三：消费者持续报错

目的：验证有限重试和死信处理。

步骤：

1. 使用测试配置或指定事件字段让 consumer 稳定抛出异常；
2. 发布一条测试事件；
3. 从日志确认总尝试次数符合配置；
4. 确认重试期间没有形成高速无限 requeue 循环；
5. 确认重试耗尽后主队列不再包含该消息；
6. 确认 DLQ 中出现一条消息，并能看到原始事件和死亡原因相关 header。

### 实验四：预约保存成功，但消息发送失败

目的：观察数据库与 RabbitMQ 双写不一致，并为 Outbox 学习建立问题背景。

步骤：

1. 停止 RabbitMQ 或使用不可用的连接配置；
2. 创建预约；
3. 确认预约数据已经提交；
4. 确认通知没有生成；
5. 从日志中定位发布失败及对应 `eventId`；
6. 记录第一轮为什么无法仅靠 Publisher Confirm 自动找回该事件。

第一轮只要求能解释和观察这个问题，不要求完成生产级补偿。

## 12. 第二轮：Transactional Outbox

第二轮将预约和待发送事件写入同一个数据库事务：

```text
同一数据库事务
    ├─ 保存 view_appointment
    └─ 插入 outbox_event，状态为 PENDING

后台投递任务
    → 查询 PENDING 事件
    → 发布到 RabbitMQ
    → 收到 confirm 且未被 return
    → 标记为 SENT
    → 失败则保留 PENDING/FAILED，并按策略再次投递
```

Outbox 解决“预约已经提交，但事件没有留下可补发记录”的问题，但它不能消除重复消息：RabbitMQ 可能已经接收消息，而进程在把 Outbox 标记为 `SENT` 之前崩溃。因此第一轮实现的消费者幂等性在第二轮仍然必须保留。

第二轮可继续增加：

- 投递次数与下次投递时间；
- 批量领取与并发锁定；
- 长期失败状态；
- 管理端查看和人工重放；
- Outbox 清理或归档策略。

## 13. 两周学习安排

### 第一周：基础收发与路由，约 6–8 小时

- 安装并启动固定版本 RabbitMQ 和 Management UI；
- 理解 Exchange、Queue、Binding、Routing Key；
- 完成 Direct/Topic 的基础路由实验；
- 使用 Spring Boot 的 `RabbitTemplate` 与 `@RabbitListener` 完成收发；
- 理解 durable、persistent、ready、unacked；
- 理解 Publisher Confirm、Return 和 Consumer Acknowledgement 的边界；
- 完成消费者停止与恢复实验。

### 第二周：预约通知与故障恢复，约 6–8 小时

- 完成预约创建事件和站内通知表；
- 完成基于 `eventId` 和唯一约束的消费者幂等；
- 配置有限次数重试、退避、DLX 和 DLQ；
- 完成重复事件实验；
- 完成消费者持续失败实验；
- 完成发布失败实验并记录一致性缺口；
- 整理日志、测试结果和故障处理说明。

之后回到 SQL、事务、测试和 CI 学习，不需要紧接着学习 Kafka 或 RocketMQ。Outbox 可以作为 RabbitMQ 的第二轮专题。

## 14. 第一轮完成标准

在不查看教学视频的情况下，能够：

1. 从预约创建到通知落库独立实现完整链路；
2. 解释 Publisher Confirm、Return 和 Consumer Acknowledgement 的区别；
3. 演示消费者停止后消息积压并在恢复后继续处理；
4. 重复发送同一个事件而只产生一条通知；
5. 让消费者稳定失败，并确认有限重试后消息进入 DLQ；
6. 解释“消息没发出去、处理失败、重复到达”三种情况分别如何处理；
7. 明确指出第一轮不能彻底解决数据库与 RabbitMQ 双写一致性，并说明 Outbox 如何补足。

## 15. 暂不纳入范围

- 微服务拆分；
- Kafka、RocketMQ 或其他消息中间件；
- 真实短信、邮件和第三方推送；
- RabbitMQ 集群和跨机房高可用；
- 高吞吐量性能调优；
- 自动消费和自动重放 DLQ；
- 第一轮完整实现 Outbox。

## 16. 版本注意事项

如果课程讲到 Lazy Queue，需要按实际 RabbitMQ 版本判断。RabbitMQ 当前已经不再支持单独的 `lazy` 模式；旧的配置会被忽略。可以学习它过去解决的问题，但不要照搬旧版本配置。

Quorum Queue 在新版本中还具有 delivery limit 等 poison-message 保护，但第一轮仍应显式设计应用重试与 DLQ，不应依赖 Broker 默认限制来代替业务错误处理。

## 17. 官方参考资料

- [RabbitMQ Consumer Acknowledgements and Publisher Confirms](https://www.rabbitmq.com/docs/confirms)
- [RabbitMQ Reliability Guide](https://www.rabbitmq.com/docs/reliability)
- [RabbitMQ Dead Letter Exchanges](https://www.rabbitmq.com/docs/dlx)
- [RabbitMQ Lazy Queues（历史说明）](https://www.rabbitmq.com/docs/lazy-queues)
- [RabbitMQ Quorum Queues](https://www.rabbitmq.com/docs/quorum-queues)
- [Spring Boot AMQP](https://docs.spring.io/spring-boot/reference/messaging/amqp.html)
- [Spring AMQP Resilience and Error Recovery](https://docs.spring.io/spring-amqp/reference/amqp/resilience-recovering-from-errors-and-broker-failures.html)
