package com.gr.freshermanagement.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Center extends Facility{
    private String address;
    private CenterStatus status;


    public enum CenterStatus{
        ACTIVE, INACTIVE
    }
}
