package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Fresher;
import org.springframework.data.domain.Page;

public interface FresherService {
    Page<EmployeeResponse> getFreshersPaginated(int page, int size);
}
