package com.gr.freshermanagement.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class FixedSaltBCrypt {
    public static String hashWithFixedSalt(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }
}

