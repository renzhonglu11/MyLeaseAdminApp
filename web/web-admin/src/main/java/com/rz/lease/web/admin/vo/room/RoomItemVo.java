package com.rz.lease.web.admin.vo.room;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.entity.RoomInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(description = "房间信息")
public class RoomItemVo extends RoomInfo {

    @Schema(description = "租约结束日期")
    private Date leaseEndDate;

    @Schema(description = "当前入住状态")
    private Boolean isCheckIn;

    @Schema(description = "所属公寓信息")
    private ApartmentInfo apartmentInfo;

}
