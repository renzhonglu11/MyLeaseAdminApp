package com.rz.lease.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaseStatus implements BaseEnum {

    SIGNING(1, "Pending Signature Confirmation"),
    SIGNED(2, "Signed"),
    CANCELED(3, "Canceled"),
    EXPIRED(4, "Expired"),
    WITHDRAWING(5, "Pending Move-out Confirmation"),
    WITHDRAWN(6, "Moved Out"),
    RENEWING(7, "Pending Renewal Confirmation");

    @JsonValue
    private final Integer code;

    private final String name;

    LeaseStatus(Integer code, String name) {
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
