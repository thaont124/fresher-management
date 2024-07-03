package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Fresher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FresherRepository extends JpaRepository<Fresher, Long> {
}
