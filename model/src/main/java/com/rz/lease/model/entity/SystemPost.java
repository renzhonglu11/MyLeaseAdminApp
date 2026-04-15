package com.rz.lease.model.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.model.enums.converter.BaseStatusConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Entity
@Table(name = "system_post")
@Getter
@Setter
public class SystemPost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Post code")
    @Column(name = "code")
    private String postCode;

    @Schema(description = "Post name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Post description")
    @Column(name = "description")
    private String description;

    @Schema(description = "Post status")
    @Column(name = "status")
    @Convert(converter = BaseStatusConverter.class)
    private BaseStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<SystemUser> systemUsers;

}
