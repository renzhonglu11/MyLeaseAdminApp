package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.LeaseStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaseStatusConverter extends BaseEnumConverter<LeaseStatus> {

    public LeaseStatusConverter() {
        super(LeaseStatus.class);
    }
}
