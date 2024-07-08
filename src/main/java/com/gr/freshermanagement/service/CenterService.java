package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CenterService {
    Page<CenterResponse> getAllCenters(int page, int size) ;

    CenterResponse addCenter(NewCenterRequest centerRequest);

    CenterResponse updateCenter(Long id, CenterUpdateRequest centerRequest);

    void deleteCenter(Long id);
}
