package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.constant.RedisConstant;
import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.web.admin.security.JwtUtils;
import com.rz.lease.web.admin.service.LoginService;
import com.rz.lease.web.admin.vo.login.CaptchaVo;
import com.rz.lease.web.admin.vo.login.LoginVo;
import com.wf.captcha.SpecCaptcha;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate; // Spring create a StringRedisTemplate bean and inject it here
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);

        String textCode = buildCaptchaValue(specCaptcha);
        String key = buildCaptchaKey();
        // Store the captcha value in Redis with a 60-second
        stringRedisTemplate.opsForValue().set(key, textCode, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SECONDS,
                TimeUnit.SECONDS);

        return new CaptchaVo(specCaptcha.toBase64(), key);
    }

    private @NonNull String buildCaptchaValue(SpecCaptcha specCaptcha) {
        String captchaText = specCaptcha.text();
        if (captchaText == null) {
            throw new IllegalStateException("captcha text must not be null");
        }
        return Objects.requireNonNull(captchaText.toLowerCase());
    }

    private @NonNull String buildCaptchaKey() {
        return RedisConstant.ADMIN_LOGIN_CAPTCHA + UUID.randomUUID();
    }

    @Override
    public String login(LoginVo loginVo) {
        if (loginVo.getCaptchaCode() == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        String captchaKey = loginVo.getCaptchaKey();
        if (captchaKey == null || captchaKey.isBlank()) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        String code = stringRedisTemplate.opsForValue().get(captchaKey);
        if (code == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }

        if (!code.equalsIgnoreCase(loginVo.getCaptchaCode())) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        String normalizedUsername = username.trim();
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(normalizedUsername, password));
        } catch (BadCredentialsException ex) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        } catch (AuthenticationException ex) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }

        return jwtUtils.generateToken(normalizedUsername);

    }
}
