package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room & facility relation table")
@Entity
@Table(name = "room_facility")
@Getter
@Setter
public class RoomFacility extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Room facility id")
    @Column(name = "facility_id")
    private Long facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", insertable = false, updatable = false)
    private FacilityInfo facility;



}
