package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class CenterHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private ActionEnum action;

    @ManyToOne
    @JoinColumn(name = "recently_center_id")
    private Center recentlyCenter;

    private LocalDateTime actionDate;

    @OneToOne
    @JoinColumn(name = "previous_center_id")
    private Center previousCenter;

    public enum ActionEnum {
        MERGED,
        DELETED
    }
}
