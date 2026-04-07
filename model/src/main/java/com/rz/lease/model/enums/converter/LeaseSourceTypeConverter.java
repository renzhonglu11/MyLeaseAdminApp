package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.LeaseSourceType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaseSourceTypeConverter extends BaseEnumConverter<LeaseSourceType> {

    public LeaseSourceTypeConverter() {
        super(LeaseSourceType.class);
    }
}
