package com.pocket.services.income.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pocket.services.income.dto.response.IncomeDtoResponse;
import com.pocket.services.income.model.Income;
import com.pocket.services.user.model.User;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>{

    Optional<Income> findByIdAndUser(Long id, User user);

    Page<IncomeDtoResponse> findAllByUser(User user, Pageable pageable);
}
