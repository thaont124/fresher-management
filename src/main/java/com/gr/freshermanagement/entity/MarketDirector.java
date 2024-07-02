package com.gr.freshermanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class MarketDirector extends Employee{
    @OneToOne
    private Market market;
}
