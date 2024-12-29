package com.pocket.services.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pocket.services.budget.model.BudgetEntry;

@Repository
public interface BudgetEntryRepository extends JpaRepository<BudgetEntry, Long> {
}
