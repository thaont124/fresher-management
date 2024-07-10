package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.CenterRepository;
import com.gr.freshermanagement.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;

    public Page<Center> getAllCenters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return centerRepository.findAll(pageable);
    }

    public Center addCenter(Center center) {
        // Additional validation or business logic can be added here
        return centerRepository.save(center);
    }

    public Center updateCenter(Long id, Center updatedCenter) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Center not found"));

        center.setName(updatedCenter.getName());
        center.setAddress(updatedCenter.getAddress());
        // Update other fields as needed

        return centerRepository.save(center);
    }

    public void deleteCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Center not found"));

        center.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(center); // Update status to INACTIVE
    }
}
