package com.gr.freshermanagement.exception.employee;

import com.gr.freshermanagement.exception.base.BadRequestException;

public class EmployeeInAnotherCenter extends BadRequestException {
    public EmployeeInAnotherCenter(){
        setMessage("Existed Employee is assigned");
    }

    public EmployeeInAnotherCenter(String message){
        setMessage(message);
    }

}
