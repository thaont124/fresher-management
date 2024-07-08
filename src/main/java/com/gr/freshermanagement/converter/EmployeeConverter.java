package com.gr.freshermanagement.converter;

import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeConverter {

    public static Fresher toFresher(NewEmployeeRequest request, Department department) {

        Fresher fresher = new Fresher();
        fresher.setName(request.getName());
        fresher.setDob(request.getDob());
        fresher.setAddress(request.getAddress());
        fresher.setPhone(request.getPhone());
        fresher.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        fresher.setEmail(request.getEmail());
        fresher.setDepartment(department);
        fresher.setStatus(EmployeeStatus.ACTIVE);
        fresher.setFresherStatus(FresherStatus.WAIT);
        fresher.generateFresherCode();
        return fresher;
    }

    public static EmployeeResponse toFresherResponse(Fresher fresher) {
        EmployeeResponse response = new EmployeeResponse();
        response.setName(fresher.getName());
        response.setDob(fresher.getDob());
        response.setAddress(fresher.getAddress());
        response.setPhone(fresher.getPhone());
        response.setGender(fresher.getGender().name());
        response.setEmail(fresher.getEmail());
        response.setStatus(fresher.getStatus().name());
        return response;
    }

    public static List<Fresher> toFresherList(List<NewEmployeeRequest> requests, Department department) {
        return requests.stream()
                .map(request -> toFresher(request, department))
                .collect(Collectors.toList());
    }

    public static List<EmployeeResponse> toFresherResponseList(List<Fresher> freshers) {
        return freshers.stream()
                .map(EmployeeConverter::toFresherResponse)
                .collect(Collectors.toList());
    }
}
