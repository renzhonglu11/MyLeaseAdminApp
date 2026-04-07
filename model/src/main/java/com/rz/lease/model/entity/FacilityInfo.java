package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.converter.ItemTypeConverter;
import java.util.List;

@Schema(description = "Facility information table")
@Entity
@Table(name = "facility_info")
@Getter
@Setter
public class FacilityInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Facility owner object type")
    @Column(name = "type")
    @Convert(converter = ItemTypeConverter.class)
    private ItemType type;

    @Schema(description = "Name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Icon")
    @Column(name = "icon")
    private String icon;

    @JsonIgnore
    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    private List<ApartmentFacility> apartmentFacilities;

    @JsonIgnore
    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    private List<RoomFacility> roomFacilities;




}
