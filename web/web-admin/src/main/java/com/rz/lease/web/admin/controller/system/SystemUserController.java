package com.rz.lease.web.admin.controller.system;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.rz.lease.web.admin.vo.system.user.SystemUserQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Backend user information management")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {

    @Operation(summary = "Query backend user list by page according to conditions")
    @GetMapping("page")
    public Result<List<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size,
            SystemUserQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "Query backend user information by ID")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "Save or update backend user information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        return Result.ok();
    }

    @Operation(summary = "Check whether the backend username is available")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        return Result.ok();
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "Delete backend user information by ID")
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "Update backend user status by ID")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
