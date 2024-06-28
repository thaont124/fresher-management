package com.gr.freshermanagement.exception.base;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        setCode("com.ncsgroup.profiling.exception.base.BadRequestException");
        setStatus(400);
    }
}
