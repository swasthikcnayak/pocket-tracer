package com.pocket.services.budget.model;

import java.util.List;

import com.pocket.services.common.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @OneToMany(mappedBy = "budget", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetEntry> budgetEntries;
}
