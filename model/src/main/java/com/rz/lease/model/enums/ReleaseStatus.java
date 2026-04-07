package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReleaseStatus implements BaseEnum {

    RELEASED(1, "Released"),
    NOT_RELEASED(0, "Not Released");

    @JsonValue
    private final Integer code;

    private final String name;

    ReleaseStatus(Integer code, String name) {
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
