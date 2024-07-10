package com.gr.freshermanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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
