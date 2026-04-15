package com.rz.lease.web.admin.vo.user;

import com.rz.lease.model.enums.BaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "User information list item")
@Data
public class UserInfoItemVo {

    @Schema(description = "Primary key")
    private Long id;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "Avatar URL")
    private String avatarUrl;

    @Schema(description = "Password")
    private String password;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Account status")
    private BaseStatus status;

    public UserInfoItemVo(Long id, String phone, String avatarUrl, String nickname,
            BaseStatus status) {
        this.id = id;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.password = null;
        this.nickname = nickname;
        this.status = status;
    }

    public UserInfoItemVo() {
    }
}
