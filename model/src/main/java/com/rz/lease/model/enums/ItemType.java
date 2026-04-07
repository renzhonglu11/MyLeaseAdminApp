package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemType implements BaseEnum {

    APARTMENT(1, "Apartment"),

    ROOM(2, "Room");

    @JsonValue
    private final Integer code;
    private final String name;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
