package com.pocket.services.budget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "budget_entry")
public class BudgetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;

    @ManyToOne
    @JoinColumn(name="budget_id", nullable = false)
    private Budget budget;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private double amount;
}
