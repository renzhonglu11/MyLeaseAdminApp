package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SystemUserType implements BaseEnum {

    ADMIN(0, "Admin"),
    COMMON(1, "Common User");

    @JsonValue
    private final Integer code;

    private final String name;

    SystemUserType(Integer code, String name) {
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
