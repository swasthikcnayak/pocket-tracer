package com.pocket.services.expense.service.analytics.taskmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.expense.model.Expense;

public class ExpenseDeleteTaskManager extends AnalyticsTaskManager {
    Logger logger = LoggerFactory.getLogger(ExpenseDeleteTaskManager.class);

    public ExpenseDeleteTaskManager(Expense expense) {
        super(expense);
    }

    @Override
    public void run() {
        logger.info("Expense deleted "+expense.getAmount());
    }

}
