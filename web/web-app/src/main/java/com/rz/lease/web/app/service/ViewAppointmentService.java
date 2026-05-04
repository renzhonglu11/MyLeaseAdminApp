package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.ViewAppointment;
import com.rz.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.rz.lease.web.app.vo.appointment.AppointmentItemVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface ViewAppointmentService {
    List<AppointmentItemVo> listItem(Long userId);

    AppointmentDetailVo getDetailById(Long id);

    void saveOrUpdate(ViewAppointment viewAppointment);
}
