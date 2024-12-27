package com.pocket.services.income.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pocket.services.common.user.model.User;
import com.pocket.services.income.dto.response.IncomeDtoResponse;
import com.pocket.services.income.model.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<Income> findIncomeByIdAndUser(Long id, User user);

    Optional<IncomeDtoResponse> findByIdAndUser(Long id, User user);

    Page<IncomeDtoResponse> findAllByUser(User user, Pageable pageable);

    int deleteByIdAndUser(Long id, User user);
}
