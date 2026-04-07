package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room & payment type relation table")
@Entity
@Table(name = "room_payment_type")
@Getter
@Setter
public class RoomPaymentType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Payment type id")
    @Column(name = "payment_type_id")
    private Long paymentTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", insertable = false, updatable = false)
    private PaymentType paymentType;



}
