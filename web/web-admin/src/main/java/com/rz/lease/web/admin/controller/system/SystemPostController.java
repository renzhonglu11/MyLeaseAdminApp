package com.rz.lease.web.admin.controller.system;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.SystemPost;
import com.rz.lease.model.enums.BaseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Backend user position management")
@RequestMapping("/admin/system/post")
public class SystemPostController {

    @Operation(summary = "Get position information by page")
    @GetMapping("page")
    private Result<List<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        return Result.ok();
    }

    @Operation(summary = "Save or update position information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        return Result.ok();
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "Delete position by ID")
    public Result removeById(@RequestParam Long id) {

        return Result.ok();
    }

    @GetMapping("getById")
    @Operation(summary = "Get position information by ID")
    public Result<SystemPost> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "Get all position list")
    @GetMapping("list")
    public Result<List<SystemPost>> list() {
        return Result.ok();
    }

    @Operation(summary = "Update status by position ID")
    @PostMapping("updateStatusByPostId")
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
