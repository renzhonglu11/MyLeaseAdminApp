package com.rz.lease.common.result;

import lombok.Getter;

/**
 * Unified return result status information class
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "Success"),
    FAIL(201, "Fail"),
    PARAM_ERROR(202, "Parameter error"),
    SERVICE_ERROR(203, "Service exception"),
    DATA_ERROR(204, "Data exception"),
    ILLEGAL_REQUEST(205, "Illegal request"),
    REPEAT_SUBMIT(206, "Repeated submission"),
    DELETE_ERROR(207, "Please delete subsets first"),

    ADMIN_ACCOUNT_EXIST_ERROR(301, "Account already exists"),
    ADMIN_CAPTCHA_CODE_ERROR(302, "Captcha code error"),
    ADMIN_CAPTCHA_CODE_EXPIRED(303, "Captcha code has expired"),
    ADMIN_CAPTCHA_CODE_NOT_FOUND(304, "Captcha code not entered"),

    ADMIN_LOGIN_AUTH(305, "Not logged in"),
    ADMIN_ACCOUNT_NOT_EXIST_ERROR(306, "Account does not exist"),
    ADMIN_ACCOUNT_ERROR(307, "Username or password error"),
    ADMIN_ACCOUNT_DISABLED_ERROR(308, "User has been disabled"),
    ADMIN_ACCESS_FORBIDDEN(309, "No access permission"),

    ADMIN_APARTMENT_DELETE_ERROR(310, "Delete the rooms under the apartment first"),

    APP_LOGIN_AUTH(501, "Not logged in"),
    APP_LOGIN_PHONE_EMPTY(502, "Phone number is empty"),
    APP_LOGIN_CODE_EMPTY(503, "Captcha code is empty"),
    APP_SEND_SMS_TOO_OFTEN(504, "Verification code sent too frequently"),
    APP_LOGIN_CODE_EXPIRED(505, "Captcha code has expired"),
    APP_LOGIN_CODE_ERROR(506, "Captcha code error"),
    APP_ACCOUNT_DISABLED_ERROR(507, "User has been disabled"),

    TOKEN_EXPIRED(601, "Token expired"),
    TOKEN_INVALID(602, "Token invalid");

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
