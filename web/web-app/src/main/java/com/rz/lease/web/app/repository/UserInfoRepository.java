package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends BaseJpaRepository<UserInfo> {
    Optional<UserInfo> findByPhone(String phone);
}
