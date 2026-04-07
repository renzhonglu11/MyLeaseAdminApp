package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentStatus implements BaseEnum {

    WAITING(1, "Pending Viewing"),

    CANCELED(2, "Canceled"),

    VIEWED(3, "Viewed");

    @JsonValue
    private final Integer code;

    private final String name;

    AppointmentStatus(Integer code, String name) {
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
