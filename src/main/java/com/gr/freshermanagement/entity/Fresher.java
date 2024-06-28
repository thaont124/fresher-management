package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Fresher extends Employee{

    @Enumerated(EnumType.STRING)
    private FresherStatus fresherStatus;

}
