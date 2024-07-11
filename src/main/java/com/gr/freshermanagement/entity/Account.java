package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public enum AccountStatus{
        NEW,
        ACTIVE,
        INACTIVE
    }
}