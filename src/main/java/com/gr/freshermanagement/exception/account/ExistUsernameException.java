package com.gr.freshermanagement.exception.account;


import com.gr.freshermanagement.exception.base.BadRequestException;

public class ExistUsernameException extends BadRequestException {
    public ExistUsernameException(){
        setMessage("Username is existed");
    }
}
