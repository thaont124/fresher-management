package com.gr.freshermanagement.service;


import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.NewFresherResponse;

public interface EmployeeService {
    NewFresherResponse createNewEmployee(NewEmployeeRequest newFresherRequest);
}


