package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room lease term relation table")
@Entity
@Table(name = "room_lease_term")
@Getter
@Setter
public class RoomLeaseTerm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Lease term id")
    @Column(name = "lease_term_id")
    private Long leaseTermId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_term_id", insertable = false, updatable = false)
    private LeaseTerm leaseTerm;

    public RoomLeaseTerm() {
    }

    public RoomLeaseTerm(Long roomId, Long leaseTermId) {
        this.roomId = roomId;
        this.leaseTermId = leaseTermId;
    }



}
