package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.EmployeeRequest;
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
    public EmployeeResponse updateFresher(Long id, Fresher updatedFresher) {
        Fresher fresher = fresherRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Fresher not found with id: " + id));

        fresher.setName(updatedFresher.getName());
        fresher.setDob(updatedFresher.getDob());
        fresher.setAddress(updatedFresher.getAddress());
        fresher.setPhone(updatedFresher.getPhone());
        fresher.setGender(updatedFresher.getGender());
        fresher.setEmail(updatedFresher.getEmail());
        fresher.setDepartment(updatedFresher.getDepartment());
        fresher.setStatus(updatedFresher.getStatus());
        fresher.setFresherStatus(updatedFresher.getFresherStatus());

        Fresher savedFresher = fresherRepository.save(fresher);

        return convertToEmployeeResponse(savedFresher);
    }

    public EmployeeResponse updateFresherInfo(Long id, EmployeeRequest employeeRequest) {
        Fresher fresher = fresherRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Fresher not found with id: " + id));


        // Update common fields
        fresher.setName(employeeRequest.getName());
        fresher.setDob(employeeRequest.getDob());
        fresher.setAddress(employeeRequest.getAddress());
        fresher.setPhone(employeeRequest.getPhone());
        fresher.setGender(Gender.valueOf(employeeRequest.getGender()));
        fresher.setEmail(employeeRequest.getEmail());

        fresher.setFresherStatus(FresherStatus.valueOf(employeeRequest.getFresherStatus()));


        // Save updated employee
        fresherRepository.save(fresher);

        // Return response
        return convertToEmployeeResponse(fresher);

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
