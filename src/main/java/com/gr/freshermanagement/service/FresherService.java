package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Fresher;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FresherService {
    List<EmployeeResponse> addListFresher(MultipartFile file);

    Page<Fresher> getFreshers(int page, int size);
}
