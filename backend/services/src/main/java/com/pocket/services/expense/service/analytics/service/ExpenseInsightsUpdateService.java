package com.pocket.services.expense.service.analytics.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.repository.ExpenseAnalyticsRepository;

@Service
public class ExpenseInsightsUpdateService {

    Logger logger = LoggerFactory.getLogger(ExpenseInsightsUpdateService.class);

    @Autowired
    ExpenseAnalyticsRepository expenseAnalyticsRepository;

    @Transactional
    public void updateExpense(List<Expense> expense) {
        try {
            this.deleteExpense(expense.get(0));
            this.addExpense(expense.get(1));
        } catch (Exception e) {
            logger.error("Exception in update expense", e);
        }
    }

    @Transactional
    public void deleteExpense(Expense expense) {
        try {
            this.expenseAnalyticsRepository.deleteExpense(
                    expense.getUser().getId(), expense.getCategory().toString(), expense.getDate().getMonthValue(),
                    expense.getDate().getYear(), expense.getAmount());
            logger.info("Expense updated : Delete");
        } catch (Exception e) {
            logger.error("Exception in delete expense", e);
        }
    }

    @Transactional
    public void addExpense(Expense expense) {
        try {
            this.expenseAnalyticsRepository.addExpense(
                    expense.getUser().getId(), expense.getCategory().toString(), expense.getDate().getMonthValue(),
                    expense.getDate().getYear(), expense.getAmount());
            logger.info("Expense updated : Add");
        } catch (Exception e) {
            logger.error("Exception in add expense", e);
        }
    }
}
