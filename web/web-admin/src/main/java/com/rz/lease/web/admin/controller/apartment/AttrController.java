package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.AttrKey;
import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.web.admin.service.AttrKeyService;
import com.rz.lease.web.admin.service.AttrValueService;
import com.rz.lease.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room attribute management")
@RestController
@RequestMapping("/admin/attr")
public class AttrController {

    private AttrKeyService attrKeyService;
    private AttrValueService attrValueService;

    public AttrController(AttrKeyService attrKeyService, AttrValueService attrValueService) {
        this.attrKeyService = attrKeyService;
        this.attrValueService = attrValueService;
    }

    @Operation(summary = "Add or update attribute name")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        attrKeyService.saveOrUpdateAttrKey(attrKey);
        return Result.ok();
    }

    @Operation(summary = "Add or update attribute value")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        attrValueService.saveOrUpdateAttrValue(attrValue);
        return Result.ok();
    }

    @Operation(summary = "Query all attribute names and attribute values list")
    @GetMapping("list")
    public Result<List<AttrKeyVo>> listAttrInfo() {
        List<AttrKeyVo> list = attrKeyService.listAttrInfo();
        return Result.ok(list);
    }

    @Operation(summary = "Delete attribute name by ID")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        if (!attrKeyService.deleteAttrKeyById(attrKeyId)) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Operation(summary = "Delete attribute value by ID")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        if (!attrValueService.deleteAttrValueById(id)) {
            return Result.fail();
        }
        return Result.ok();
    }

}
