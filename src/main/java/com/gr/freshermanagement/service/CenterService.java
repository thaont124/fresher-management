package com.gr.freshermanagement.service;

import com.gr.freshermanagement.entity.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CenterService {
    Page<Center> getAllCenters(int page, int size);

    Center addCenter(Center center);

    void deleteCenter(Long id);

    Center updateCenter(Long id, Center center);
}
