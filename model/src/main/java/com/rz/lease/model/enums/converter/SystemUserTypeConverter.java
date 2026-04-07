package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.SystemUserType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SystemUserTypeConverter extends BaseEnumConverter<SystemUserType> {

    public SystemUserTypeConverter() {
        super(SystemUserType.class);
    }
}
