package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus;

    public Role(String name, RoleStatus roleStatus) {
        this.name = name;
        this.roleStatus = roleStatus;
    }
    public enum RoleStatus{
        ACTIVE,
        INACTIVE
    }
}
