package com.rz.lease.model.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "District information table")
@Entity
@Table(name = "district_info")
@Getter
@Setter
public class DistrictInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "District name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Associated city id")
    @Column(name = "city_id")
    private Integer cityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    @JsonIgnore
    private CityInfo city;

    @JsonIgnore
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    private List<ApartmentInfo> apartments;

}
