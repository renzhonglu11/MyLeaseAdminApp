package com.rz.lease.web.admin.service;

import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.vo.user.UserInfoItemVo;
import com.rz.lease.web.admin.vo.user.UserInfoQueryVo;
import org.springframework.data.domain.Page;

/**
 * @author rz
 * @description 针对表【user_info(用户信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface UserInfoService {

    Page<UserInfoItemVo> page(long current, long size, UserInfoQueryVo queryVo);

    void updateStatusById(Long id, BaseStatus status);
}
