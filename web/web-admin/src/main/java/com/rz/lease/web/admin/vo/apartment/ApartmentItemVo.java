package com.rz.lease.web.admin.vo.apartment;

import com.rz.lease.model.enums.ReleaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "后台管理系统公寓列表实体")
public class ApartmentItemVo {

    @Schema(description = "Primary key")
    private Long id;

    @Schema(description = "Apartment name")
    private String name;

    @Schema(description = "Apartment description")
    private String introduction;

    @Schema(description = "District id")
    private Long districtId;

    @Schema(description = "District name")
    private String districtName;

    @Schema(description = "City id")
    private Long cityId;

    @Schema(description = "City name")
    private String cityName;

    @Schema(description = "Province id")
    private Long provinceId;

    @Schema(description = "Province name")
    private String provinceName;

    @Schema(description = "Detailed address")
    private String addressDetail;

    @Schema(description = "Longitude")
    private String latitude;

    @Schema(description = "Latitude")
    private String longitude;

    @Schema(description = "Apartment front desk phone")
    private String phone;

    @Schema(description = "Published")
    private ReleaseStatus isRelease;

    @Schema(description = "房间总数")
    private Long totalRoomCount;

    @Schema(description = "空闲房间数")
    private Long freeRoomCount;

}
