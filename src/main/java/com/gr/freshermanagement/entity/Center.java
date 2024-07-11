package com.gr.freshermanagement.entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private CenterStatus status;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Center market;


    public enum CenterStatus{
        ACTIVE, INACTIVE
    }
}
