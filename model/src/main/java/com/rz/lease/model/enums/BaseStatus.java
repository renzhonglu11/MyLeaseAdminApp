package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BaseStatus implements BaseEnum {

    ENABLE(1, "Enabled"),

    DISABLE(0, "Disabled");

    @JsonValue
    private final Integer code;

    private final String name;

    BaseStatus(Integer code, String name) {
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
