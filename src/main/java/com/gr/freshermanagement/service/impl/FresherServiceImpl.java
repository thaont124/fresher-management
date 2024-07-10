package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.converter.EmployeeConverter;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Department;
import com.gr.freshermanagement.entity.Fresher;
import com.gr.freshermanagement.repository.DepartmentRepository;
import com.gr.freshermanagement.repository.FresherRepository;
import com.gr.freshermanagement.service.FresherService;
import com.gr.freshermanagement.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FresherServiceImpl implements FresherService {
    private final FresherRepository fresherRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<EmployeeResponse> addListFresher(MultipartFile file) {
        try {
            //get requests from excel
            List<NewEmployeeRequest> requests = ExcelUtils.excelToFresherList(file.getInputStream());

            //get all department
            List<String> departmentCodes = requests.stream()
                    .map(NewEmployeeRequest::getDepartmentCode)
                    .toList();
            List<Department> departments = departmentRepository.findAllByCodeIn(departmentCodes);
            Map<String, Department> departmentMap = departments.stream()
                    .collect(Collectors.toMap(Department::getCode, department -> department));

            //convert to fresher and save
            List<Fresher> freshers = requests.stream()
                    .map(request -> {
                        Department department = departmentMap.get(request.getDepartmentCode());
                        if (department == null) {
                            throw new RuntimeException("Department not found for id: " + request.getDepartmentCode());
                        }
                        return EmployeeConverter.toFresher(request, department);
                    })
                    .collect(Collectors.toList());
            freshers = fresherRepository.saveAll(freshers);
            return freshers.stream()
                    .map(EmployeeConverter::toFresherResponse)
                    .collect(Collectors.toList());

        } catch (IOException ex) {
            throw new RuntimeException("Excel data is failed to store: " + ex.getMessage());
        }
    }
}
