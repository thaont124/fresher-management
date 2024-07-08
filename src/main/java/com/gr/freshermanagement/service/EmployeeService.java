package com.gr.freshermanagement.service;


import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmployeeService {
    NewFresherResponse createNewEmployee(NewEmployeeRequest newFresherRequest);

    EmployeeResponse getInfo(Long accountId);

    EmployeeResponse updateInfo(EmployeeUpdateRequest request, Long accountId);

    NewFresherResponse createListEmployee(MultipartFile file) throws IOException;
}


