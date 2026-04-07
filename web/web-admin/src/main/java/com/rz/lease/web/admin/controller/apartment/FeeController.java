package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.FeeKey;
import com.rz.lease.model.entity.FeeValue;
import com.rz.lease.web.admin.service.FeeKeyService;
import com.rz.lease.web.admin.service.FeeValueService;
import com.rz.lease.web.admin.vo.fee.FeeKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room miscellaneous fee management")
@RestController
@RequestMapping("/admin/fee")
public class FeeController {

    private final FeeKeyService feeKeyService;
    private final FeeValueService feeValueService;

    public FeeController(FeeKeyService feeKeyService, FeeValueService feeValueService) {
        this.feeKeyService = feeKeyService;
        this.feeValueService = feeValueService;
    }

    @Operation(summary = "Save or update miscellaneous fee name")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateFeeKey(@RequestBody FeeKey feeKey) {
        feeKeyService.saveOrUpdateFeeKey(feeKey);
        return Result.ok();
    }

    @Operation(summary = "Save or update miscellaneous fee value")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateFeeValue(@RequestBody FeeValue feeValue) {
        feeValueService.saveOrUpdateFeeValue(feeValue);
        return Result.ok();
    }

    @Operation(summary = "Query all miscellaneous fee names and values list")
    @GetMapping("list")
    public Result<List<FeeKeyVo>> feeInfoList() {
        List<FeeKeyVo> list = feeKeyService.listFeeInfo();
        return Result.ok(list);
    }

    @Operation(summary = "Delete miscellaneous fee name by ID")
    @DeleteMapping("key/deleteById")
    public Result deleteFeeKeyById(@RequestParam Long feeKeyId) {
        if (!feeKeyService.deleteFeeKeyById(feeKeyId)) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Operation(summary = "Delete miscellaneous fee value by ID")
    @DeleteMapping("value/deleteById")
    public Result deleteFeeValueById(@RequestParam Long id) {
        if (!feeValueService.deleteFeeValueById(id)) {
            return Result.fail();
        }
        return Result.ok();
    }
}
