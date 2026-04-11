package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.service.ApartmentInfoService;
import com.rz.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Apartment information management")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    private ApartmentInfoService apartmentInfoService;

    public ApartmentController(ApartmentInfoService apartmentInfoService) {
        this.apartmentInfoService = apartmentInfoService;
    }

    @Operation(summary = "Save or update apartment information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        apartmentInfoService.saveOrUpdateApartmentInfo(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "Query apartment list by page according to conditions")
    @GetMapping("pageItem")
    public Result<Page<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size,
            ApartmentQueryVo queryVo) {
        return Result.ok(apartmentInfoService.pageItem(current, size, queryVo));
    }

    @Operation(summary = "Get apartment details by ID")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo detailVo = apartmentInfoService.getDetailById(id);
        return Result.ok(detailVo);
    }

    @Operation(summary = "Delete apartment information by ID")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        apartmentInfoService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "Update apartment release status by ID")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        apartmentInfoService.updateReleaseStatusById(id, status);
        return Result.ok();
    }

    @Operation(summary = "Query apartment information list by district ID")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentItemVo>> listInfoByDistrictId(@RequestParam Long id) {
        List<ApartmentItemVo> apartmentInfos = apartmentInfoService.listInfoByDistrictId(id);

        return Result.ok(apartmentInfos);
    }
}
