package com.rz.lease.common.exception;

import com.rz.lease.common.result.ResultCodeEnum;

import lombok.Data;

@Data
public class LeaseException extends RuntimeException {

    private Integer code;

    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

}
