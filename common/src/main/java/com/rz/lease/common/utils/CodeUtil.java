package com.rz.lease.common.utils;

import java.security.SecureRandom;

public final class CodeUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private CodeUtil() {
    }

    public static String getRandomCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}
