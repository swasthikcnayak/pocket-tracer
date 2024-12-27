package com.pocket.services.budget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="budget_id")
    private Budget budget;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private double amount;
}
