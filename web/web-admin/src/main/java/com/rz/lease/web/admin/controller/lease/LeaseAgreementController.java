package com.rz.lease.web.admin.controller.lease;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.admin.service.LeaseAgreementService;
import com.rz.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.rz.lease.web.admin.vo.agreement.AgreementVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lease agreement management")
@RestController
@RequestMapping("/admin/agreement")
public class LeaseAgreementController {

    private LeaseAgreementService leaseAgreementService;

    public LeaseAgreementController(LeaseAgreementService leaseAgreementService) {
        this.leaseAgreementService = leaseAgreementService;
    }

    @Operation(summary = "Save or modify lease agreement information")
    @PostMapping("saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        leaseAgreementService.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }

    @Operation(summary = "Query lease agreement list by page according to conditions")
    @GetMapping("page")
    public Result<Page<AgreementVo>> page(@RequestParam long current, @RequestParam long size,
            AgreementQueryVo queryVo) {

        return Result.ok(leaseAgreementService.page(current, size, queryVo));
    }

    @Operation(summary = "Query lease agreement information by ID")
    @GetMapping(name = "getById")
    public Result<AgreementVo> getById(@RequestParam Long id) {
        AgreementVo agreementVo = leaseAgreementService.getAgreementById(id);
        return Result.ok(agreementVo);
    }

    @Operation(summary = "Delete lease agreement information by ID")
    @DeleteMapping("removeById")
    public Result<Void> removeById(@RequestParam Long id) {
        boolean isRemoved = leaseAgreementService.removeById(id);
        if (!isRemoved) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Operation(summary = "Update lease agreement status by ID")
    @PostMapping("updateStatusById")
    public Result<Void> updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        boolean isUpdated = leaseAgreementService.updateStatusById(id, status);
        if (!isUpdated) {
            return Result.fail();
        }
        return Result.ok();
    }

}
