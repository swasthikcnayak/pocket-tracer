package com.pocket.services.expense.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.services.expense.dto.response.ExpenseDtoResponse;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.user.model.User;


public interface ExpenseRepository extends JpaRepository<Expense, Long>{

    Optional<Expense> findByIdAndUser(Long id, User user);

    Page<ExpenseDtoResponse> findAllByUser(User user, Pageable pageable);
}
