package com.pocket.services.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pocket.services.expense.dto.response.ExpenseAnalyticsByCategoryDto;
import com.pocket.services.expense.model.ExpenseAnalytics;
import jakarta.transaction.Transactional;

@Repository
public interface ExpenseAnalyticsRepository extends JpaRepository<ExpenseAnalytics, Long> {

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

        @Query(value = """
                        SELECT category AS category, SUM(total_amount) AS totalAmount
                        FROM expense_insights
                        WHERE user_id = :userId AND year = :year
                        GROUP BY category HAVING totalAmount > 0""", nativeQuery = true)
        List<ExpenseAnalyticsByCategoryDto> getAnalyticsForUserByYear(Long userId, int year);

        @Query(value = """
                        SELECT category AS category, SUM(total_amount) AS totalAmount
                        FROM expense_insights
                        WHERE user_id = :userId AND year = :year AND month = :month
                        GROUP BY category HAVING totalAmount > 0""", nativeQuery = true)
        List<ExpenseAnalyticsByCategoryDto> getAnalyticsForUserByMonthAndYear(Long userId, int month, int year);
}
