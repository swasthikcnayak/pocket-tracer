package com.pocket.services.expense.service.analytics.taskmanager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.service.analytics.service.ExpenseInsightsUpdateService;

public class ExpenseDeleteTaskManager extends AnalyticsTaskManager {
    Logger logger = LoggerFactory.getLogger(ExpenseDeleteTaskManager.class);

    public ExpenseDeleteTaskManager(List<Expense> expense,  ExpenseInsightsUpdateService expenseInsightsUpdateService) {
        super(expense, expenseInsightsUpdateService);
    }

    @Override
    public void run() {
        logger.info("EXPENSE DELETED");
        expenseInsightsUpdateService.deleteExpense(this.expense.get(0));
    }

}
