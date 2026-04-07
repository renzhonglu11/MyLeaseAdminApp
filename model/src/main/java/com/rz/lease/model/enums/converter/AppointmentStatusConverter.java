package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.AppointmentStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AppointmentStatusConverter extends BaseEnumConverter<AppointmentStatus> {

    public AppointmentStatusConverter() {
        super(AppointmentStatus.class);
    }
}
