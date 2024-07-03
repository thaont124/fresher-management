package com.gr.freshermanagement.service;

import com.gr.freshermanagement.entity.Fresher;
import org.springframework.data.domain.Page;

public interface FresherService {
    Page<Fresher> getFreshersPaginated(int page, int size);
}
