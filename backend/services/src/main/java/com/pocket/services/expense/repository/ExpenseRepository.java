package com.pocket.services.expense.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pocket.services.common.user.model.User;
import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findExpenseByIdAndUser(Long id, User user);

    Optional<ExpenseDtoResponse> findByIdAndUser(Long id, User user);

    int deleteByIdAndUser(Long id, User user);

    Page<ExpenseDtoResponse> findAllByUser(User user, Pageable pageable);

    @Query(value = """
            SELECT e FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDateTime AND :endDateTime
            """, nativeQuery = false)
    List<Expense> findExpensesByUserAndDateTimeBetween(User user, LocalDateTime startDateTime,
            LocalDateTime endDateTime);
}
