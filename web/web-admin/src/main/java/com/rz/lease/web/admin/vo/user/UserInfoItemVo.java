package com.rz.lease.web.admin.vo.user;

import com.rz.lease.model.enums.BaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "User information list item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoItemVo {

    @Schema(description = "Primary key")
    private Long id;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "Avatar URL")
    private String avatarUrl;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Account status")
    private BaseStatus status;
}
