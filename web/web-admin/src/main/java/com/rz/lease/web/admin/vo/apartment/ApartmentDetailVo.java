package com.rz.lease.web.admin.vo.apartment;

import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.vo.graph.GraphVo;
import com.rz.lease.web.admin.vo.fee.FeeValueVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "公寓信息")
@Getter
@Setter
public class ApartmentDetailVo {

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

    @Schema(description = "图片列表")
    private List<GraphVo> graphVoList;

    @Schema(description = "标签列表")
    private List<LabelInfo> labelInfoList;

    @Schema(description = "配套列表")
    private List<FacilityInfo> facilityInfoList;

    @Schema(description = "杂费列表")
    private List<FeeValueVo> feeValueVoList;

}
