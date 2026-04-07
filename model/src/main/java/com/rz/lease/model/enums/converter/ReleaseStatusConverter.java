package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.ReleaseStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReleaseStatusConverter extends BaseEnumConverter<ReleaseStatus> {

    public ReleaseStatusConverter() {
        super(ReleaseStatus.class);
    }
}
