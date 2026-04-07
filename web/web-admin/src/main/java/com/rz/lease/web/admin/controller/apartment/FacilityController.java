package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.admin.service.FacilityInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facility management")
@RestController
@RequestMapping("/admin/facility")
public class FacilityController {

    private FacilityInfoService facilityInfoService;

    public FacilityController(FacilityInfoService facilityInfoService) {
        this.facilityInfoService = facilityInfoService;
    }

    @Operation(summary = "[By type] Query facility information list")
    @GetMapping("list")
    public Result<List<FacilityInfo>> listFacility(@RequestParam(required = false) ItemType type) {
        return Result.ok(facilityInfoService.listFacilityInfo(type));
    }

    @Operation(summary = "Add or modify facility information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        facilityInfoService.saveOrUpdateFacilityInfo(facilityInfo);
        return Result.ok();
    }

    @Operation(summary = "Delete facility information by ID")
    @DeleteMapping("deleteById")
    public Result removeFacilityById(@RequestParam Long id) {
        if (!facilityInfoService.deleteFacilityInfoById(id)) {
            return Result.fail();
        }
        return Result.ok();
    }

}
