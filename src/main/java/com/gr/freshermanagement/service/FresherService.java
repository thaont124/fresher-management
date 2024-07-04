package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Fresher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface FresherService {
    Page<EmployeeResponse> getFreshersPaginated(int page, int size);
    Page<EmployeeResponse> getFreshersByFacilityAndDateRange(Long facilityId, LocalDate startDate, LocalDate endDate, int page, int size);

    @Transactional
    void deactivateFresher(Long fresherId);

    @Transactional
    EmployeeResponse updateFresher(Long id, Fresher updatedFresher);
}
