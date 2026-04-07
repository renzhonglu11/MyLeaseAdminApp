package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaseSourceType implements BaseEnum {

    NEW(1, "New Lease"),
    RENEW(2, "Renewal");

    @JsonValue
    private final Integer code;

    private final String name;

    LeaseSourceType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
