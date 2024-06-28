package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.ManagerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerHistoryRepository extends JpaRepository<ManagerHistory, Long> {
}
