package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Room basic attribute value table")
@Entity
@Table(name = "attr_value")
@Getter
@Setter
public class AttrValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Attribute value")
    @Column(name = "name")
    private String name;

    @Schema(description = "Associated attribute key id")
    @Column(name = "attr_key_id")
    private Long attrKeyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attr_key_id", insertable = false, updatable = false)
    @JsonIgnore
    private AttrKey attrKey;

    @JsonIgnore
    @OneToMany(mappedBy = "attrValue", fetch = FetchType.LAZY)
    private List<RoomAttrValue> roomAttrValues;




}
