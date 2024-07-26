package com.gr.freshermanagement.exception.account;

import com.gr.freshermanagement.exception.base.BadRequestException;

public class EmailNotValid extends BadRequestException {
    public EmailNotValid(){
        setMessage("Email is not valid");
    }

    public EmailNotValid(String message){
        setMessage(message);
    }
}
