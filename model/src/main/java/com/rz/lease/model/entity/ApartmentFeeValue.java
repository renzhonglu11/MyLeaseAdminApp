package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Apartment&FeeValueRelation")
@Entity
@Table(name = "apartment_fee_value")
@Getter
@Setter
public class ApartmentFeeValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Fee value id")
    @Column(name = "fee_value_id")
    private Long feeValueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    private ApartmentInfo apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_value_id", insertable = false, updatable = false)
    private FeeValue feeValue;




}
