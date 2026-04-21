package com.rz.lease.web.admin.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
admin: test123
use: 123abc
jason: 1995106x
*/

public class GeneratePassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "1995106x";
        String bcryptHash = encoder.encode(rawPassword);

        System.out.println("Raw: " + rawPassword);
        System.out.println("BCrypt: " + bcryptHash);
    }

}
