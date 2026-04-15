package com.rz.lease.web.admin.controller.system;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.Result;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.model.entity.SystemPost;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.service.SystemPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Backend user position management")
@RequestMapping("/admin/system/post")
public class SystemPostController {

    private SystemPostService systemPostService;

    public SystemPostController(SystemPostService systemPostService) {
        this.systemPostService = systemPostService;
    }

    @Operation(summary = "Get position information by page")
    @GetMapping("page")
    public Result<Page<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        return Result.ok(systemPostService.page(current, size));
    }

    @Operation(summary = "Save or update position information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        systemPostService.saveOrUpdate(systemPost);
        return Result.ok();
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "Delete position by ID")
    public Result removeById(@RequestParam Long id) {
        boolean isRemoved = systemPostService.removeById(id);
        if (!isRemoved) {
            return Result.fail();
        }
        return Result.ok();
    }

    @GetMapping("getById")
    @Operation(summary = "Get position information by ID")
    public Result<SystemPost> getById(@RequestParam Long id) {
        SystemPost systemPost = systemPostService.getById(id);
        if (systemPost == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        return Result.ok(systemPost);
    }

    @Operation(summary = "Get all position list")
    @GetMapping("list")
    public Result<List<SystemPost>> list() {
        return Result.ok(systemPostService.list());
    }

    @Operation(summary = "Update status by position ID")
    @PostMapping("updateStatusByPostId")
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
