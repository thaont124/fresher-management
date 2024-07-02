package com.gr.freshermanagement.exception.department;

import com.gr.freshermanagement.exception.base.NotFoundException;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException(){
        setMessage("Department not found");
    }
}
