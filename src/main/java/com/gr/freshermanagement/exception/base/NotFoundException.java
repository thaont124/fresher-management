package com.gr.freshermanagement.exception.base;

import com.gr.freshermanagement.exception.BaseException;

public class NotFoundException extends BaseException {
    public NotFoundException() {
        setCode("com.ncsgroup.profiling.exception.base.BadRequestException");
        setStatus(404);
    }

    public NotFoundException(String message){
        setMessage(message);
    }
}
