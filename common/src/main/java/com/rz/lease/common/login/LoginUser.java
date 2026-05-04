package com.rz.lease.common.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUser {
    private final Long userId;
    private final String username;
}
