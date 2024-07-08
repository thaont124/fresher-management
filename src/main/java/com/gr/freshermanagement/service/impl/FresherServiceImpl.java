package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.EmployeeStatus;
import com.gr.freshermanagement.entity.Fresher;
import com.gr.freshermanagement.entity.FresherStatus;
import com.gr.freshermanagement.entity.Gender;
import com.gr.freshermanagement.repository.FresherRepository;
import com.gr.freshermanagement.service.FresherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FresherServiceImpl implements FresherService {
    @Autowired
    private FresherRepository fresherRepository;

    @Override
    public Page<EmployeeResponse> getFreshersPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        Page<Fresher> freshersPage = fresherRepository.findAll(pageRequest);
        return freshersPage.map(this::convertToEmployeeResponse);
    }

    @Override
    public Page<EmployeeResponse> getFreshersByFacilityAndDateRange(Long facilityId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Fresher> fresherPage = fresherRepository.findFreshersByFacilityAndDateRange(facilityId, startDate, endDate, pageable);
        // Assuming you have a method to convert Fresher to EmployeeResponse
        return fresherPage.map(this::convertToEmployeeResponse);
    }


    @Transactional
    @Override
    public void deactivateFresher(Long fresherId) {
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(() -> new UsernameNotFoundException("Fresher not found with id: " + fresherId));

        fresher.setStatus(EmployeeStatus.INACTIVE);
        fresherRepository.save(fresher);
    }

    @Transactional
    @Override
    public EmployeeResponse updateFresher(Long id, EmployeeUpdateRequest employeeRequest) {
        Fresher fresher = fresherRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Fresher not found with id: " + id));

        // Update common fields if they are not null
        if (employeeRequest.getName() != null) {
            fresher.setName(employeeRequest.getName());
        }
        if (employeeRequest.getDob() != null) {
            fresher.setDob(employeeRequest.getDob());
        }
        if (employeeRequest.getAddress() != null) {
            fresher.setAddress(employeeRequest.getAddress());
        }
        if (employeeRequest.getPhone() != null) {
            fresher.setPhone(employeeRequest.getPhone());
        }
        if (employeeRequest.getGender() != null) {
            fresher.setGender(Gender.valueOf(employeeRequest.getGender()));
        }
        if (employeeRequest.getEmail() != null) {
            fresher.setEmail(employeeRequest.getEmail());
        }
        if (employeeRequest.getFresherStatus() != null) {
            fresher.setFresherStatus(FresherStatus.valueOf(employeeRequest.getFresherStatus()));
        }

        // Save updated fresher
        Fresher savedFresher = fresherRepository.save(fresher);

        // Return response
        return convertToEmployeeResponse(savedFresher);
    }


    private EmployeeResponse convertToEmployeeResponse(Fresher fresher) {
        return new EmployeeResponse(
                fresher.getId(),
                fresher.getName(),
                fresher.getEmployeeCode(),
                fresher.getDob(),
                fresher.getAddress(),
                fresher.getPhone(),
                fresher.getGender().toString(),
                fresher.getEmail(),
                fresher.getDepartment().getName(),
                fresher.getStatus().toString(),
                fresher.getFresherStatus().toString()
        );
    }
}
