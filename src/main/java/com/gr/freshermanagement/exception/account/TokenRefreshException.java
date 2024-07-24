package com.gr.freshermanagement.exception.account;

import com.gr.freshermanagement.exception.BaseException;

public class TokenRefreshException extends BaseException {
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        setMessage("Failed for [" +  token + "]: " +  message);
    }
}
