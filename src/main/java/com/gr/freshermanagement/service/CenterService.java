package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.entity.Center;

import java.util.List;

public interface CenterService {
    List<CenterResponse> getAllCenters(int page, int size);

    CenterResponse addCenter(NewCenterRequest center);

    void deleteCenter(Long id);

    CenterResponse updateCenter(Long id, CenterUpdateRequest center);
}
