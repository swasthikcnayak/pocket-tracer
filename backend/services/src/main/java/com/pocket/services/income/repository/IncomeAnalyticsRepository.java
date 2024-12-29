package com.pocket.services.income.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pocket.services.income.dto.response.IncomeAnalyticsByCategoryDto;
import com.pocket.services.income.model.IncomeAnalytics;

import jakarta.transaction.Transactional;

@Repository
public interface IncomeAnalyticsRepository extends JpaRepository<IncomeAnalytics, Long> {

        @Modifying
        @Transactional
        @Query(value = """
                        INSERT INTO income_insights (user_id, category, month, year, total_amount)
                        VALUES (:userId, :category, :month, :year, :amount)
                        ON DUPLICATE KEY UPDATE total_amount = total_amount + VALUES(total_amount)
                        """, nativeQuery = true)
        void addIncome(Long userId, String category, int month, int year, double amount);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE income_insights
                        SET total_amount = total_amount - :amount
                        WHERE user_id = :userId AND category = :category AND month = :month AND year = :year
                        AND total_amount >= :amount
                        """, nativeQuery = true)
        void deleteIncome(Long userId, String category, int month, int year, double amount);

        @Query(value = """
                        SELECT category AS category, SUM(total_amount) AS totalAmount
                        FROM income_insights
                        WHERE user_id = :userId AND year = :year
                        GROUP BY category HAVING totalAmount > 0""", nativeQuery = true)
        List<IncomeAnalyticsByCategoryDto> getAnalyticsForUserByYear(Long userId, int year);

        @Query(value = """
                        SELECT category AS category, SUM(total_amount) AS totalAmount
                        FROM income_insights
                        WHERE user_id = :userId AND year = :year AND month = :month
                        GROUP BY category HAVING totalAmount > 0""", nativeQuery = true)
        List<IncomeAnalyticsByCategoryDto> getAnalyticsForUserByMonthAndYear(Long userId, int month, int year);
}
