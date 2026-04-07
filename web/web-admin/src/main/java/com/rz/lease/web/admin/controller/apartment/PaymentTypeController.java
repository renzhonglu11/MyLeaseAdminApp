package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.web.admin.service.PaymentTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payment method management")
@RequestMapping("/admin/payment")
@RestController
public class PaymentTypeController {

    private PaymentTypeService service;

    public PaymentTypeController(PaymentTypeService service) {
        this.service = service;
    }

    @Operation(summary = "Query all payment method list")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        List<PaymentType> list = service.listPaymentType();
        return Result.ok(list);
    }

    @Operation(summary = "Save or update payment method")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
        service.saveOrUpdatePaymentType(paymentType);
        return Result.ok();
    }

    @Operation(summary = "Delete payment method by ID")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        boolean isDeleted = service.deletePaymentTypeById(id);
        return Result.ok(isDeleted);
    }

}
