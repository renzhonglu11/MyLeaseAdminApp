package com.rz.lease.web.admin.controller.user;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.service.UserInfoService;
import com.rz.lease.web.admin.vo.user.UserInfoItemVo;
import com.rz.lease.web.admin.vo.user.UserInfoQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User information management")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    private UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "Query user information by page")
    @GetMapping("page")
    public Result<Page<UserInfoItemVo>> pageUserInfo(@RequestParam long current, @RequestParam long size,
            UserInfoQueryVo queryVo) {
        return Result.ok(userInfoService.page(current, size, queryVo));
    }

    @Operation(summary = "Update account status by user ID")
    @PostMapping("updateStatusById")
    public Result<Void> updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        userInfoService.updateStatusById(id, status);
        return Result.ok();
    }
}
