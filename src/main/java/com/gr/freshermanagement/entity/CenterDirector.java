package com.gr.freshermanagement.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CenterDirector extends Employee{
    @OneToOne
    private Center center;
}
