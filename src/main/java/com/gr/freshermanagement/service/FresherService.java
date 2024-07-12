package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.ListAssignFresherRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface FresherService {
    List<EmployeeResponse> addListFresher(MultipartFile file);

    List<EmployeeResponse> getFreshers(int page, int size);

    List<EmployeeResponse> filterFresher(int page, int size, Long centerId, LocalDate startDate, LocalDate endDate, String languageName);

    void assignFresherToCenter(ListAssignFresherRequest request);
}
