package com.gr.freshermanagement.exception.center;

import com.gr.freshermanagement.exception.base.NotFoundException;

public class CenterNotFoundException  extends NotFoundException {
    public CenterNotFoundException(){
        setMessage("WorkingSpace not found");
    }

    public CenterNotFoundException(String message){
        setMessage(message);
    }
}
