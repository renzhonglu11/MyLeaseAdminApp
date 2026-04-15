package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.rz.lease.web.admin.vo.system.user.SystemUserQueryVo;

/**
 * @author rz
 * @description 针对表【system_user(员工信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface SystemUserService {

    void saveOrUpdate(SystemUser systemUser);

    List<SystemUserItemVo> page(long current, long size, SystemUserQueryVo queryVo);

    SystemUserItemVo getById(Long id);

    Boolean isUsernameExists(String username);

    boolean removeById(Long id);

    void updateStatusByUserId(Long id, BaseStatus status);
}
