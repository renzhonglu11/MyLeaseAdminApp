package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.admin.service.LabelInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Label management")
@RestController
@RequestMapping("/admin/label")
public class LabelController {

    private LabelInfoService service;

    public LabelController(LabelInfoService service) {
        this.service = service;
    }

    @Operation(summary = "(By type) Query label list")
    @GetMapping("list")
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {

        return Result.ok(service.listLabelInfo(type));
    }

    @Operation(summary = "Add or modify label information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        service.saveOrUpdateLabelInfo(labelInfo);
        return Result.ok();
    }

    @Operation(summary = "Delete label information by ID")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        if (!service.deleteLabelInfoById(id)) {
            return Result.fail();
        }
        return Result.ok();
    }
}
