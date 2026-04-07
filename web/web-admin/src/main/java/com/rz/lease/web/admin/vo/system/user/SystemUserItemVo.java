package com.rz.lease.web.admin.vo.system.user;

import com.rz.lease.model.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "后台管理系统用户基本信息实体")
public class SystemUserItemVo extends SystemUser {

    @Schema(description = "岗位名称")
    private String postName;

}
