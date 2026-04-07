package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Province information table")
@Entity
@Table(name = "province_info")
@Getter
@Setter
public class ProvinceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Province name")
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    private List<CityInfo> cities;

    @JsonIgnore
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    private List<ApartmentInfo> apartments;


}
