# Lease 管理后台

Lease 租赁管理平台的 Web 管理端，用于管理公寓、房间、租约、看房预约、租客与后台用户。

本目录是 Lease 单仓库的前端部分；整体架构、后端启动方式和 Docker 配置请参阅项目根目录的 [README.zh-CN.md](../README.zh-CN.md)。

## 技术栈

- Vue 3 + TypeScript
- Vite 4
- Element Plus
- Pinia
- Vue Router
- Axios
- Sass

## 环境要求

- Node.js 18+
- npm
- 默认运行在 `http://localhost:8080` 的 Lease 管理端 API

## 配置

在项目根目录执行：

```bash
cp rentHouseAdmin/.env.example rentHouseAdmin/.env.development
```

根据本地环境编辑 `rentHouseAdmin/.env.development`：

| 变量 | 用途 | 默认示例 |
| --- | --- | --- |
| `VITE_APP_NODE_ENV` | 当前运行环境 | `development` |
| `VITE_APP_TITLE` | 管理后台标题 | `后台管理` |
| `VITE_APP_BASE_URL` | Vite 将 `/admin` 请求代理到的后端地址 | `http://localhost:8080` |
| `VITE_AMAP_MAP_KEY` | 高德地图 Web 端 Key | 需要地图功能时填写 |
| `VITE_AMAP_MAP_SECRET_KEY` | 高德地图安全密钥 | 需要地图功能时填写 |

`.env*` 本地配置已被 Git 忽略。请勿把真实 Key、Token 或生产环境凭据写入可提交文件。

## 安装与启动

```bash
cd rentHouseAdmin
npm ci
npm run dev
```

Vite 默认开发地址为 `http://localhost:5173`。开发模式会启用本地 mock 插件，并将 `/admin` 请求代理到 `VITE_APP_BASE_URL`。

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `npm run dev` | 启动本地开发服务器 |
| `npm run build` | 构建生产产物到 `dist/` |
| `npm run preview` | 本地预览生产构建 |
| `npm run lint` | 检查 `src/` 中的 ESLint 问题 |
| `npm run fix` | 自动修复可修复的 ESLint 问题 |
| `npm run format` | 使用 Prettier 格式化前端文件 |
| `npm run lint:style` | 检查并修复样式问题 |

## 目录结构

```text
rentHouseAdmin/
├── mock/                    # 本地 mock 接口
├── public/                  # 静态资源
├── src/
│   ├── api/                 # 后端 API 请求封装
│   ├── assets/              # 图片、图标和字体
│   ├── components/          # 通用组件
│   ├── config/              # 全局前端配置
│   ├── directives/          # Vue 指令
│   ├── enums/               # 枚举和常量
│   ├── hooks/               # 组合式函数
│   ├── layouts/             # 管理后台布局
│   ├── router/              # 路由配置
│   ├── store/               # Pinia 状态管理
│   ├── styles/              # 全局样式
│   ├── typings/             # TypeScript 类型声明
│   ├── utils/               # 通用工具
│   └── views/               # 业务页面
├── .env.example             # 本地环境变量模板
├── package.json             # npm 依赖与命令
└── vite.config.ts           # Vite 配置
```

## 质量检查

提交前至少执行：

```bash
npm run lint
npm run build
```

`node_modules/` 和 `dist/` 是本地生成内容，不会进入 Git 仓库。
