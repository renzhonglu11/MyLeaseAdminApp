package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.ViewAppointment;
import com.rz.lease.model.enums.AppointmentStatus;
import com.rz.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.rz.lease.web.admin.vo.appointment.AppointmentVo;

/**
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface ViewAppointmentService {

    void updateStatusById(Long id, AppointmentStatus status);
}
