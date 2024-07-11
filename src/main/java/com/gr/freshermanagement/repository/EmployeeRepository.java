package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAccount(Account account);

    @Query("SELECT wh.employee FROM WorkingHistory wh " +
            "JOIN EmployeeLanguage el ON wh.employee.id = el.employee.id " +
            "WHERE wh.center.id = :centerId AND " +
            "(wh.startTime BETWEEN :startDate AND :endDate OR " +
            "(wh.endTime IS NULL OR wh.endTime BETWEEN :startDate AND :endDate)) AND " +
            "wh.employee.position = 'Fresher'  AND " +
            "el.language.languageName = :languageName")
    Page<Employee> findFreshersByCenterAndDateRangeAndLanguage(@Param("centerId") Long centerId,
                                                              @Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate,
                                                              @Param("languageName") String languageName,
                                                              Pageable pageable);


    Employee findByEmail(String email);
}
