package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.DistrictInfo;
import com.rz.lease.model.entity.ProvinceInfo;
import com.rz.lease.web.admin.service.CityInfoService;
import com.rz.lease.web.admin.service.DistrictInfoService;
import com.rz.lease.web.admin.service.ProvinceInfoService;
import com.rz.lease.web.admin.vo.apartment.CityInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Region information management")
@RestController
@RequestMapping("/admin/region")
public class RegionInfoController {

    private ProvinceInfoService provinceInfoService;
    private CityInfoService cityInfoService;
    private DistrictInfoService districtInfoService;

    public RegionInfoController(ProvinceInfoService provinceInfoService, CityInfoService cityInfoService,
            DistrictInfoService districtInfoService) {
        this.provinceInfoService = provinceInfoService;
        this.cityInfoService = cityInfoService;
        this.districtInfoService = districtInfoService;
    }

    @Operation(summary = "Query province information list")
    @GetMapping("province/list")
    public Result<List<ProvinceInfo>> listProvince() {
        return Result.ok(provinceInfoService.listProvinceInfo());
    }

    @Operation(summary = "Query city information list by province ID")
    @GetMapping("city/listByProvinceId")
    public Result<List<CityInfoDTO>> listCityInfoByProvinceId(@RequestParam Long id) {
        List<CityInfoDTO> cityInfos = cityInfoService.listCityInfoByProvinceId(id);
        return Result.ok(cityInfos);
    }

    @GetMapping("district/listByCityId")
    @Operation(summary = "Query district information by city ID")
    public Result<List<DistrictInfo>> listDistrictInfoByCityId(@RequestParam Long id) {
        return Result.ok(districtInfoService.listDistrictInfoByCityId(id));
    }

}
