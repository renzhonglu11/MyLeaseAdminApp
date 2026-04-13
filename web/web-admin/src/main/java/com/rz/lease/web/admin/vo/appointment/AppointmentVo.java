package com.rz.lease.web.admin.vo.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(description = "预约看房信息")
public class AppointmentVo {

    @Schema(description = "Primary key")
    private Long id;

    @Schema(description = "User id")
    private Long userId;

    @Schema(description = "User name")
    private String name;

    @Schema(description = "User phone number")
    private String phone;

    @Schema(description = "Apartment id")
    private Long apartmentId;

    @Schema(description = "Appointment time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    @Schema(description = "Remarks")
    private String additionalInfo;

    @Schema(description = "Appointment status")
    private AppointmentStatus appointmentStatus;

    @Schema(description = "预约公寓信息")
    private ApartmentInfo apartmentInfo;

}
