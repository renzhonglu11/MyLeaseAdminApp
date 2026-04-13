package com.rz.lease.model.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.model.enums.converter.BaseStatusConverter;
import java.util.List;

@Schema(description = "User information table")
@Entity
@Table(name = "user_info")
@Getter
@Setter
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Phone number (used as login username)")
    @Column(name = "phone")
    private String phone;

    @Schema(description = "Password")
    @Column(name = "password")
    private String password;

    @Schema(description = "Avatar URL")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Schema(description = "Nickname")
    @Column(name = "nickname")
    private String nickname;

    @Schema(description = "Account status")
    @Column(name = "status")
    @Convert(converter = BaseStatusConverter.class)
    private BaseStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BrowsingHistory> browsingHistories;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ViewAppointment> viewAppointments;

}
