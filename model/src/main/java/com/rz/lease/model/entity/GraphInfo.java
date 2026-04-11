package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.model.enums.converter.ItemTypeConverter;

@Schema(description = "Image information table")
@Entity
@Table(name = "graph_info")
@Getter
@Setter
public class GraphInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Image name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Image owner object type")
    @Column(name = "item_type")
    @Convert(converter = ItemTypeConverter.class)
    private ItemType itemType;

    @Schema(description = "Image owner object id")
    @Column(name = "item_id")
    private Long itemId;

    @Schema(description = "Image URL")
    @Column(name = "url")
    private String url;



}
