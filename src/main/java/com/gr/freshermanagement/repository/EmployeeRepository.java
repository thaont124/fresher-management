package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.Fresher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAccount(Account account);
}
