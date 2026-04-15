package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.service.SystemUserService;
import com.rz.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.rz.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.rz.lease.web.admin.repository.SystemUserRepository;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public void saveOrUpdate(SystemUser systemUser) {
        Long id = systemUser.getId();
        if (id == null) {
            if (systemUser.getPassword() != null && !systemUser.getPassword().isBlank()) {
                systemUser.setPassword(DigestUtils.md5Hex(systemUser.getPassword()));
            }
            systemUserRepository.save(systemUser);
            return;
        }

        SystemUser existing = systemUserRepository.findById(id)
                .orElseThrow(() -> new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR));

        existing.setUsername(systemUser.getUsername());
        if (systemUser.getPassword() != null && !systemUser.getPassword().isBlank()) {
            existing.setPassword(DigestUtils.md5Hex(systemUser.getPassword()));
        }
        existing.setName(systemUser.getName());
        existing.setType(systemUser.getType());
        existing.setPhone(systemUser.getPhone());
        existing.setAvatarUrl(systemUser.getAvatarUrl());
        existing.setAdditionalInfo(systemUser.getAdditionalInfo());
        existing.setPostId(systemUser.getPostId());
        existing.setStatus(systemUser.getStatus());
        systemUserRepository.save(existing);
    }

    @Override
    public List<SystemUserItemVo> page(long current, long size, SystemUserQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        String name = queryVo == null ? null : queryVo.getName();
        String phone = queryVo == null ? null : queryVo.getPhone();
        if (name != null) {
            name = name.trim();
        }
        if (phone != null) {
            phone = phone.trim();
        }

        Page<SystemUserItemVo> systemUserPage = systemUserRepository.page(name, phone, pageRequest);
        if (systemUserPage.isEmpty()) {
            return Collections.emptyList();
        }
        return systemUserPage.getContent();
    }

    public SystemUserItemVo getById(Long id) {
        return systemUserRepository.getItemById(id)
                .orElseThrow(() -> new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR));
    }

    @Override
    public Boolean isUsernameExists(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return systemUserRepository.existsByUsernameIgnoreCaseAndNotDeleted(username.trim());
    }

    @Override
    public boolean removeById(Long id) {
        if (systemUserRepository.existsById(id)) {
            systemUserRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void updateStatusByUserId(Long id, BaseStatus status) {
        SystemUser systemUser = systemUserRepository.findById(id)
                .orElseThrow(() -> new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR));

        systemUser.setStatus(status);
        systemUserRepository.save(systemUser);
    }
}
