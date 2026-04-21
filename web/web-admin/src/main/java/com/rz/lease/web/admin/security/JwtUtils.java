package com.rz.lease.web.admin.security;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final String SECRET = "my-secret-key-my-secret-key-32bytes"; // hardcode first
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username) {
        Date now = new Date();
        Date expireAt = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(SIGNING_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SIGNING_KEY)
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

    public static void main(String[] args) {
        JwtUtils jwtUtils = new JwtUtils();
        String token = jwtUtils.generateToken("user");
        System.out.println("Generated token: " + token);
    }
}
