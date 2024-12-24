package com.pocket.services.income.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.services.income.model.Income;


public interface IncomeRepository extends JpaRepository<Income, Long>{
    
}
