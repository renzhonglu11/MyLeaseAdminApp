package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room & label relation table")
@Entity
@Table(name = "room_label")
@Getter
@Setter
public class RoomLabel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Label id")
    @Column(name = "label_id")
    private Long labelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", insertable = false, updatable = false)
    private LabelInfo label;



}
