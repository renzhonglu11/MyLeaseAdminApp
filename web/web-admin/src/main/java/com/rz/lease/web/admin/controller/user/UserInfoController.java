package com.rz.lease.web.admin.controller.user;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.UserInfo;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.vo.user.UserInfoQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@Tag(name = "User information management")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    @Operation(summary = "Query user information by page")
    @GetMapping("page")
    public Result<List<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size,
            UserInfoQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "Update account status by user ID")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
