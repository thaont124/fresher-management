package com.gr.freshermanagement.exception.base;

import com.gr.freshermanagement.exception.BaseException;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        setCode("com.ncsgroup.profiling.exception.base.UnauthorizedException");
        setStatus(401);
    }
}