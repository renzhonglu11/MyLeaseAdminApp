package com.rz.lease.web.app.service.impl;

import com.rz.lease.common.constant.RedisConstant;
import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.common.utils.CodeUtil;
import com.rz.lease.common.utils.JwtUtil;
import com.rz.lease.model.entity.UserInfo;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.app.repository.UserInfoRepository;
import com.rz.lease.web.app.service.LoginService;
import com.rz.lease.web.app.vo.user.LoginVo;
import com.rz.lease.web.app.vo.user.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public void sendCode(String phone) {
        String code = CodeUtil.getRandomCode(6);
        String key = RedisConstant.APP_LOGIN_PREFIX + phone;

        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey) {
            Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (ttl > RedisConstant.APP_LOGIN_CODE_TTL_SEC - RedisConstant.APP_LOGIN_CODE_RESEND_TIME_SEC) {
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }

        stringRedisTemplate.opsForValue().set(key, code, RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);
        log.info("Generated login verification code for phone {}: {}", phone, code);
    }

    @Override
    @Transactional
    public String login(LoginVo loginVo) {

        if (!StringUtils.hasLength(loginVo.getPhone())) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }

        if (!StringUtils.hasLength(loginVo.getCode())) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }


        String key = RedisConstant.APP_LOGIN_PREFIX + loginVo.getPhone();
        String code = stringRedisTemplate.opsForValue().get(key);

        if (!StringUtils.hasLength(code)) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }

        if (!loginVo.getCode().equals(code)) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        UserInfo userInfo = userInfoRepository.findByPhone(loginVo.getPhone()).orElse(null);

        if (userInfo == null) {
            //注册
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setNickname("尚庭公寓-" + loginVo.getPhone().substring(7));
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfo = userInfoRepository.save(userInfo);
        } else {
            //禁用？
            if (userInfo.getStatus() == BaseStatus.DISABLE) {
                throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
            }
        }

        return JwtUtil.createToken(userInfo.getId(), userInfo.getPhone());
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoVo getLoginUserInfoById(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserInfo not found"));
        return  new UserInfoVo(userInfo.getNickname(),userInfo.getAvatarUrl());
    }
}
