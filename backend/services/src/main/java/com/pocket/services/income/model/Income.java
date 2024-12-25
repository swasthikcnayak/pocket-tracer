package com.pocket.services.income.model;

import java.time.LocalDate;

import com.pocket.services.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;



@Getter
@Setter
@Entity(name = "income")
public class Income{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn( 
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User user;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="category", nullable = false)
    private Category category;
    
    @Column(name="date", nullable = false)
    private LocalDate date;

    @Column(name="description", nullable = true)
    private String description;
}
