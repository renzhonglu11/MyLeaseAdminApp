package com.rz.lease.web.admin.controller.login;

import com.rz.lease.common.result.Result;
import com.rz.lease.web.admin.service.LoginService;
import com.rz.lease.web.admin.vo.login.CaptchaVo;
import com.rz.lease.web.admin.vo.login.LoginVo;
import com.rz.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Backend management system login management")
@RestController
@RequestMapping("/admin")
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Get graphic verification code")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {

        CaptchaVo captchaVo = loginService.getCaptcha();
        return Result.ok(captchaVo);
    }

    @Operation(summary = "Login")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String jwt = loginService.login(loginVo);
        return Result.ok(jwt);
    }

    @Operation(summary = "Get logged in user personal information")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        return Result.ok();
    }
}