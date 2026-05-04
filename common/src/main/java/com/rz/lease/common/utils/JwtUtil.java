package com.rz.lease.common.utils;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.login.LoginUser;
import com.rz.lease.common.result.ResultCodeEnum;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public final class JwtUtil {

    private static final String SECRET = "my-secret-key-my-secret-key-32bytes";
    private static final long EXPIRE_SECONDS = 60L * 60 * 24;

    private JwtUtil() {
    }

    public static String createToken(Long userId, String username) {
        long expireAt = Instant.now().getEpochSecond() + EXPIRE_SECONDS;
        String payload = userId + ":" + username + ":" + expireAt;
        String encodedPayload = base64Url(payload);
        return encodedPayload + "." + sign(encodedPayload);
    }

    public static LoginUser parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }

        String[] parts = token.split("\\.");
        if (parts.length != 2 || !sign(parts[0]).equals(parts[1])) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String[] fields = payload.split(":", 3);
        if (fields.length != 3) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }

        long expireAt = Long.parseLong(fields[2]);
        if (Instant.now().getEpochSecond() > expireAt) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }

        return new LoginUser(Long.parseLong(fields[0]), fields[1]);
    }

    private static String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to sign token", e);
        }
    }
}
