package com.rz.lease.web.admin.controller.lease;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.rz.lease.web.admin.vo.agreement.AgreementVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Lease agreement management")
@RestController
@RequestMapping("/admin/agreement")
public class LeaseAgreementController {

    @Operation(summary = "Save or modify lease agreement information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        return Result.ok();
    }

    @Operation(summary = "Query lease agreement list by page according to conditions")
    @GetMapping("page")
    public Result<List<AgreementVo>> page(@RequestParam long current, @RequestParam long size,
            AgreementQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "Query lease agreement information by ID")
    @GetMapping(name = "getById")
    public Result<AgreementVo> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "Delete lease agreement information by ID")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "Update lease agreement status by ID")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        return Result.ok();
    }

}
