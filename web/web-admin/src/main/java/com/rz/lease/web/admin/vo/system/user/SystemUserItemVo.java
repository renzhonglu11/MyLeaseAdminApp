package com.rz.lease.web.admin.vo.system.user;

import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.model.enums.SystemUserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "后台管理系统用户基本信息实体")
public class SystemUserItemVo {

    @Schema(description = "Primary key")
    private Long id;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Password")
    private String password;

    @Schema(description = "Name")
    private String name;

    @Schema(description = "User type")
    private SystemUserType type;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "Avatar URL")
    private String avatarUrl;

    @Schema(description = "Remarks")
    private String additionalInfo;

    @Schema(description = "Post id")
    private Long postId;

    @Schema(description = "Account status")
    private BaseStatus status;

    @Schema(description = "岗位名称")
    private String postName;

    public SystemUserItemVo(Long id, String username, String name, SystemUserType type, String phone,
            String avatarUrl, String additionalInfo, Long postId, BaseStatus status, String postName) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.additionalInfo = additionalInfo;
        this.postId = postId;
        this.status = status;
        this.postName = postName;
    }

    public SystemUserItemVo() {
    }

}
