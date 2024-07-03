package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.entity.Fresher;
import com.gr.freshermanagement.repository.FresherRepository;
import com.gr.freshermanagement.service.FresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FresherServiceImpl implements FresherService {
    @Autowired
    private FresherRepository fresherRepository;
    @Override
    public Page<Fresher> getFreshersPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        return fresherRepository.findAll(pageRequest);
    }
}
