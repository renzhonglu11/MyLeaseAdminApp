package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.converter.ItemTypeConverter;
import jakarta.persistence.*;

import java.util.List;

@Schema(description = "Label information table")
@Entity
@Table(name = "label_info")
@Getter
@Setter
public class LabelInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Type")
    @Column(name = "type")
    @Convert(converter = ItemTypeConverter.class)
    private ItemType type;

    @Schema(description = "Label name")
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "label", fetch = FetchType.LAZY)
    private List<ApartmentLabel> apartmentLabels;

    @JsonIgnore
    @OneToMany(mappedBy = "label", fetch = FetchType.LAZY)
    private List<RoomLabel> roomLabels;



}
