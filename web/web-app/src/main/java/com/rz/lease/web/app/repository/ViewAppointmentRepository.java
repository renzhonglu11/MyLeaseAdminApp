package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.ViewAppointment;

import java.util.List;

public interface ViewAppointmentRepository extends BaseJpaRepository<ViewAppointment> {
    List<ViewAppointment> findByUserIdOrderByAppointmentTimeDesc(Long userId);
}
