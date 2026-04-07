package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room & basic attribute value relation table")
@Entity
@Table(name = "room_attr_value")
@Getter
@Setter
public class RoomAttrValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Attribute value id")
    @Column(name = "attr_value_id")
    private Long attrValueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attr_value_id", insertable = false, updatable = false)
    private AttrValue attrValue;



}
