package com.pocket.services.expense.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pocket.services.expense.model.Category;
import com.pocket.services.expense.model.ExpenseInsights;

import jakarta.transaction.Transactional;

@Repository
public interface ExpenseInsightsRepository extends JpaRepository<ExpenseInsights, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO expense_insights (user_id, category, month, year, total_amount)
        VALUES (:userId, :category, :month, :year, :amount)
        ON DUPLICATE KEY UPDATE total_amount = total_amount + VALUES(total_amount)
        """, nativeQuery = true)
    void addExpense(Long userId, String category, int month, int year, double amount);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE expense_insights
    SET total_amount = total_amount - :amount
    WHERE user_id = :userId AND category = :category AND month = :month AND year = :year
    AND total_amount >= :amount
    """, nativeQuery = true)
    void deleteExpense(Long userId, String category, int month, int year, double amount);
}