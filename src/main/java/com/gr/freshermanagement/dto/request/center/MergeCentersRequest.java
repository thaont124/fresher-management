package com.gr.freshermanagement.dto.request.center;

import lombok.Data;

@Data
public class MergeCentersRequest {
    private Long centerAId;
    private Long centerBId;
    private String newCenterName;
    private String newCenterAddress;
    private MergeType mergeType;

    public enum MergeType {
        CREATE_NEW_CENTER,
        MERGE_B_INTO_A,
        MERGE_A_INTO_B
    }

}