package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.MergeCentersRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.entity.CenterHistory;
import com.gr.freshermanagement.entity.WorkingHistory;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.CenterHistoryRepository;
import com.gr.freshermanagement.repository.CenterRepository;
import com.gr.freshermanagement.repository.WorkingHistoryRepository;
import com.gr.freshermanagement.service.CenterService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;
    private final CenterHistoryRepository centerHistoryRepository;
    private final WorkingHistoryRepository workingHistoryRepository;

    @Override
    public List<CenterResponse> getAllCenters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Center> centerPage = centerRepository.findAll(pageable);
        return centerPage.stream()
                .map(fresher -> MapperUtils.toDTO(fresher, CenterResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CenterResponse addCenter(NewCenterRequest request) {
        Center center;
        if (request.getMarketId() != null) {
            Center market = centerRepository.findById(request.getMarketId())
                    .orElseThrow(() -> new NotFoundException("Market not found"));
            center = Center.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .market(market)
                    .status(Center.CenterStatus.ACTIVE)
                    .build();
        } else {
            center = Center.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .market(null)
                    .status(Center.CenterStatus.ACTIVE)
                    .build();
        }
        return MapperUtils.toDTO(centerRepository.save(center), CenterResponse.class);
    }

    @Override
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

    @Override
    public void deleteCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Center not found"));

        center.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(center);
    }

    @Transactional
    @Override
    public CenterResponse mergeCenters(MergeCentersRequest request) {
        Center centerA = centerRepository.findById(request.getCenterAId())
                .orElseThrow(() -> new NotFoundException("Center not found with id: " + request.getCenterAId()));
        Center centerB = centerRepository.findById(request.getCenterBId())
                .orElseThrow(() -> new NotFoundException("Center not found with id: " + request.getCenterBId()));

        if(centerA.getStatus().equals(Center.CenterStatus.INACTIVE)){
            throw new NotFoundException("Center is inactive with id: " + request.getCenterAId());
        }

        if(centerB.getStatus().equals(Center.CenterStatus.INACTIVE)){
            throw new NotFoundException("Center is inactive with id: " + request.getCenterBId());

        }
        Center newCenter = switch (request.getMergeType()) {
            case CREATE_NEW_CENTER ->
                    createNewCenter(request.getNewCenterName(), request.getNewCenterAddress(), centerA, centerB);
            case MERGE_B_INTO_A -> mergeIntoExistingCenter(centerA, centerB);
            case MERGE_A_INTO_B -> mergeIntoExistingCenter(centerB, centerA);
        };

        LocalDateTime timeMerger = LocalDateTime.now();
        transferEmployees(centerA, newCenter, timeMerger);
        transferEmployees(centerB, newCenter, timeMerger);

        saveCenterHistory(centerA, newCenter, timeMerger);
        saveCenterHistory(centerB, newCenter, timeMerger);

        return MapperUtils.toDTO(newCenter, CenterResponse.class);
    }

    private Center createNewCenter(String name, String address, Center centerA, Center centerB) {
        Center newCenter = new Center();
        newCenter.setName(name);
        newCenter.setAddress(address);
        newCenter.setStatus(Center.CenterStatus.ACTIVE);

        centerRepository.save(newCenter);

        centerA.setStatus(Center.CenterStatus.INACTIVE);
        centerB.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(centerA);
        centerRepository.save(centerB);

        return newCenter;
    }

    private Center mergeIntoExistingCenter(Center targetCenter, Center mergingCenter) {
        mergingCenter.setStatus(Center.CenterStatus.INACTIVE);
        centerRepository.save(mergingCenter);

        return targetCenter;
    }

    private void saveCenterHistory(Center previousCenter, Center recentlyCenter, LocalDateTime timeMerger) {
        CenterHistory history = new CenterHistory();
        history.setUpdateAt(timeMerger);
        history.setAction(CenterHistory.ActionEnum.MERGED);
        history.setRecentlyCenter(recentlyCenter);
        history.setPreviousCenter(previousCenter);

        centerHistoryRepository.save(history);
    }

    private void transferEmployees(Center oldCenter, Center newCenter, LocalDateTime timeMerger) {
        List<WorkingHistory> workingHistories = workingHistoryRepository.findCurrentlyEmployeeInCenter(oldCenter.getId());

        for (WorkingHistory oldHistory : workingHistories) {
            // Create new working history for the new center
            WorkingHistory newHistory = new WorkingHistory();
            newHistory.setEmployee(oldHistory.getEmployee());
            newHistory.setCenter(newCenter);
            newHistory.setStartTime(LocalDate.from(timeMerger));
            newHistory.setStatus(oldHistory.getStatus());  // Retain the original status


            // End the current working history
            oldHistory.setEndTime(LocalDate.from(timeMerger));
            oldHistory.setStatus(WorkingHistory.WorkingStatus.TERMINATED);
            workingHistoryRepository.save(newHistory);
            workingHistoryRepository.save(oldHistory);

        }
    }
}
