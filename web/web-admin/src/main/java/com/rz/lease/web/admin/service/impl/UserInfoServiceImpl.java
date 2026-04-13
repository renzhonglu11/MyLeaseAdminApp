package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.repository.UserInfoRepository;
import com.rz.lease.web.admin.service.UserInfoService;
import com.rz.lease.web.admin.vo.user.UserInfoItemVo;
import com.rz.lease.web.admin.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<UserInfoItemVo> page(long current, long size, UserInfoQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        String phone = queryVo == null ? null : queryVo.getPhone();
        var status = queryVo == null ? null : queryVo.getStatus();
        return userInfoRepository.pageItems(phone, status, pageRequest);
    }

    @Override
    public void updateStatusById(Long id, BaseStatus status) {
        var userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR));

        userInfo.setStatus(status);
        userInfoRepository.save(userInfo);
    }
}
