package com.rz.lease.web.admin.vo.apartment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "City information DTO")
public class CityInfoDTO {

    @Schema(description = "City ID")
    private Long id;

    @Schema(description = "City name")
    private String name;

    @Schema(description = "Associated province id")
    private Integer provinceId;
}
