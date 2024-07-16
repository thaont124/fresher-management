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

    @Query("SELECT wh FROM WorkingHistory wh "
            + "WHERE wh.center.id = :centerId AND "
            + "wh.endTime IS NULL AND "
            + "(wh.status = 'WORK' "
            + "OR wh.status = 'EDUCATE' "
            + "OR wh.status = 'MANAGE')")
    List<WorkingHistory> findCurrentlyEmployeeInCenter(@Param("centerId") Long centerId);

}
