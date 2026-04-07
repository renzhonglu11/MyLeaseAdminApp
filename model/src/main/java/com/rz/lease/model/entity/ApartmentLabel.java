package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Apartment label relation table")
@Entity
@Table(name = "apartment_label")
@Getter
@Setter
public class ApartmentLabel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Label id")
    @Column(name = "label_id")
    private Long labelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    private ApartmentInfo apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", insertable = false, updatable = false)
    private LabelInfo label;




}
