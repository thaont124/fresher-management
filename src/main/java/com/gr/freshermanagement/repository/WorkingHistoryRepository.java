package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.WorkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkingHistoryRepository extends JpaRepository<WorkingHistory, Long> {

}
