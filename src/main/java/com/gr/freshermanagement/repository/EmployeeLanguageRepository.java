package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.EmployeeLanguage;
import com.gr.freshermanagement.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeLanguageRepository extends JpaRepository<EmployeeLanguage, Long> {
    EmployeeLanguage findByEmployeeAndLanguage(Employee employee, Language language);
}
