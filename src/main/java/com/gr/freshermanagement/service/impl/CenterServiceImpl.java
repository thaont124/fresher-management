package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.CenterRepository;
import com.gr.freshermanagement.service.CenterService;
import com.gr.freshermanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;

    public List<CenterResponse> getAllCenters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Center> centerPage = centerRepository.findAll(pageable);
        return centerPage.stream()
                .map(fresher -> MapperUtils.toDTO(fresher, CenterResponse.class))
                .collect(Collectors.toList());
    }

    public CenterResponse addCenter(NewCenterRequest request) {
        Center market = centerRepository.findById(request.getMarketId())
                .orElseThrow(() -> new NotFoundException("Market not found"));
        Center center = Center.builder()
                .name(request.getName())
                .address(request.getAddress())
                .market(market)
                .build();
        return MapperUtils.toDTO(centerRepository.save(center), CenterResponse.class);
    }

    public CenterResponse updateCenter(Long id, CenterUpdateRequest updatedCenter) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Center not found"));

        if (updatedCenter.getName() != null) {
            center.setName(updatedCenter.getName());
        }
        if (updatedCenter.getAddress() != null) {
            center.setAddress(updatedCenter.getAddress());
        }
        if (updatedCenter.getStatus() != null) {
            center.setStatus(Center.CenterStatus.valueOf(updatedCenter.getStatus()));
        }
        return MapperUtils.toDTO(centerRepository.save(center), CenterResponse.class);
    }

    public void deleteCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Center not found"));

        center.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(center);
    }
}
