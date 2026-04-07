package com.rz.lease.model.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "City information table")
@Entity
@Table(name = "city_info")
@Getter
@Setter
public class CityInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "City name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Associated province id")
    @Column(name = "province_id")
    private Integer provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProvinceInfo province;

    @JsonIgnore
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<DistrictInfo> districts;

    @JsonIgnore
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<ApartmentInfo> apartments;

}
