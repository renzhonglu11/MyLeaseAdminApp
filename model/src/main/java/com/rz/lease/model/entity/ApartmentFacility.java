package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Apartment&FacilityRelation")
@Entity
@Table(name = "apartment_facility")
@Getter
@Setter
public class ApartmentFacility extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Facility id")
    @Column(name = "facility_id")
    private Long facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    private ApartmentInfo apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", insertable = false, updatable = false)
    private FacilityInfo facility;




}
