package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Room basic attribute table")
@Entity
@Table(name = "attr_key")
@Getter
@Setter
public class AttrKey extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Attribute key")
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "attrKey", fetch = FetchType.LAZY)
    private List<AttrValue> attrValues;


}
