package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.exception.facility.FacilityNotFoundException;
import com.gr.freshermanagement.repository.CenterRepository;
import com.gr.freshermanagement.service.CenterService;
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

    @Override
    public Page<CenterResponse> getAllCenters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Center> centersPage = centerRepository.findAll(pageable);
        return centersPage.map(CenterResponse::new);
    }

    @Override
    public CenterResponse addCenter(NewCenterRequest centerRequest) {
        Center center = new Center();
        center.setName(centerRequest.getName());
        center.setAddress(centerRequest.getAddress());
        center.setStatus(Center.CenterStatus.valueOf(centerRequest.getStatus()));
        centerRepository.save(center);
        return new CenterResponse(center);
    }

    @Override
    public CenterResponse updateCenter(Long id, CenterUpdateRequest centerRequest) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Center not found with id: " + id));

        if (centerRequest.getName() != null) center.setName(centerRequest.getName());
        if (centerRequest.getAddress() != null) center.setAddress(centerRequest.getAddress());
        if (centerRequest.getStatus() != null) center.setStatus(
                Center.CenterStatus.valueOf(centerRequest.getStatus()));

        centerRepository.save(center);
        return new CenterResponse(center);
    }

    @Override
    public void deleteCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Center not found with id: " + id));

        center.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(center);
    }
}
