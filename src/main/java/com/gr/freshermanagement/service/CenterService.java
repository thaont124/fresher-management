package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.CenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;

import java.util.List;

public interface CenterService {
    List<CenterResponse> getAllCenters();

    CenterResponse addCenter(CenterRequest centerRequest);

    CenterResponse updateCenter(Long id, CenterRequest centerRequest);

    void deleteCenter(Long id);
}
