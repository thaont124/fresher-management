package com.gr.freshermanagement.exception.base;

import com.gr.freshermanagement.exception.BaseException;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        setCode("com.gr.freshermanagement.exception.base.BadRequestException");
        setStatus(400);
    }
}
