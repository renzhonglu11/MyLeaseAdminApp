package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.web.admin.service.SystemUserService;
import com.rz.lease.web.admin.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class SystemUserServiceImpl implements SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;
}
