package com.gr.freshermanagement.exception.account;


import com.gr.freshermanagement.exception.base.BadRequestException;

public class UsernamePasswordIncorrectException extends BadRequestException {
    public UsernamePasswordIncorrectException(){
        setMessage("Username or Password is incorrect");
    }
}
