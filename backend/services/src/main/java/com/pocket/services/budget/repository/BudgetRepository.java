package com.pocket.services.budget.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pocket.services.budget.model.Budget;
import com.pocket.services.common.user.model.User;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    public Optional<Budget> findByUserAndMonthAndYear(User user, int month, int year);

    public Optional<Budget> findByUserAndId(User user, Long id);
}
