package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.BaseStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BaseStatusConverter extends BaseEnumConverter<BaseStatus> {

    public BaseStatusConverter() {
        super(BaseStatus.class);
    }
}
