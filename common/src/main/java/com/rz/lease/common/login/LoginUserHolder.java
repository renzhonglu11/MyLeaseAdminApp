package com.rz.lease.common.login;

public final class LoginUserHolder {

    private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

    private LoginUserHolder() {
    }

    public static void setLoginUser(LoginUser loginUser) {
        LOGIN_USER.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return LOGIN_USER.get();
    }

    public static void clear() {
        LOGIN_USER.remove();
    }
}
