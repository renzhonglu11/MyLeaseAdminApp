package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.web.admin.service.LeaseTermService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lease term management")
@RequestMapping("/admin/term")
@RestController
public class LeaseTermController {

    private LeaseTermService service;

    public LeaseTermController(LeaseTermService service) {
        this.service = service;
    }

    @GetMapping("list")
    @Operation(summary = "Query all lease term list")
    public Result<List<LeaseTerm>> listLeaseTerm() {
        List<LeaseTerm> list = service.listLeaseTerm();
        return Result.ok(list);
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "Save or update lease term information")
    public Result saveOrUpdate(@RequestBody LeaseTerm leaseTerm) {
        service.saveOrUpdateLeaseTerm(leaseTerm);
        return Result.ok();
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "Delete lease term by ID")
    public Result deleteLeaseTermById(@RequestParam Long id) {
        service.deleteLeaseTermById(id);
        return Result.ok();
    }
}
