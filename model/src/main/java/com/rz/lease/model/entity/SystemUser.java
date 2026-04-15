package com.rz.lease.model.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.model.enums.SystemUserType;
import com.rz.lease.model.enums.converter.BaseStatusConverter;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Staff information")
@Entity
@Table(name = "system_user")
@Getter
@Setter
public class SystemUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Username")
    @Column(name = "username")
    private String username;

    @Schema(description = "Password")
    @Column(name = "password")
    private String password;

    @Schema(description = "Name")
    @Column(name = "name")
    private String name;

    @Schema(description = "User type")
    @Column(name = "type")
    private SystemUserType type;

    @Schema(description = "Phone number")
    @Column(name = "phone")
    private String phone;

    @Schema(description = "Avatar URL")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Schema(description = "Remarks")
    @Column(name = "additional_info")
    private String additionalInfo;

    @Schema(description = "Post id")
    @Column(name = "post_id")
    private Long postId;

    @Schema(description = "Account status")
    @Column(name = "status")
    @Convert(converter = BaseStatusConverter.class)
    private BaseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @JsonIgnore
    private SystemPost post;

}
