package com.rz.lease.web.admin.security;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final String USER_ID_CLAIM = "userId";
    private final SecretKey signingKey;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return generateToken(username, null);
    }

    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expireAt = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
        Map<String, Object> claims = new HashMap<>();
        if (userId != null) {
            claims.put(USER_ID_CLAIM, userId);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> {
            Object userId = claims.get(USER_ID_CLAIM);
            if (userId instanceof Number number) {
                return number.longValue();
            }
            return null;
        });
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (userDetails == null) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
        final String username = extractUsername(token);
        if (!username.equals(userDetails.getUsername())) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
        if (isTokenExpired(token)) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }
        return true;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
