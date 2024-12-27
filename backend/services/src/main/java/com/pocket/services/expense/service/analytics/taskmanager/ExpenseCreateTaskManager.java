package com.pocket.services.expense.service.analytics.taskmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.expense.model.Expense;

public class ExpenseCreateTaskManager extends AnalyticsTaskManager {
    Logger logger = LoggerFactory.getLogger(ExpenseCreateTaskManager.class);

    public ExpenseCreateTaskManager(Expense expense) {
        super(expense);
    }

    @Override
    public void run() {
        logger.info("Expense created "+expense.getAmount());
    }

}
