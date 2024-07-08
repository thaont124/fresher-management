package com.gr.freshermanagement.service;


import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    NewFresherResponse createNewEmployee(NewEmployeeRequest newFresherRequest);

    EmployeeResponse getInfo(Long accountId);

    EmployeeResponse updateInfo(EmployeeUpdateRequest request, Long accountId);

    List<EmployeeResponse> createListEmployee(MultipartFile file) throws IOException;
}


