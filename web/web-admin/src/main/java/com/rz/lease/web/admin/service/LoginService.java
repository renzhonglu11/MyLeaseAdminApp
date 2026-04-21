package com.rz.lease.web.admin.service;

import com.rz.lease.web.admin.vo.login.CaptchaVo;
import com.rz.lease.web.admin.vo.login.LoginVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);
}
