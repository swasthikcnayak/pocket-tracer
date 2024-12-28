package com.pocket.services.expense.service.analytics.taskmanager;

import java.util.List;

import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.service.analytics.service.ExpenseInsightsUpdateService;

public abstract class AnalyticsTaskManager implements Runnable {
    List<Expense> expense;
    ExpenseInsightsUpdateService expenseInsightsUpdateService;

    public AnalyticsTaskManager(List<Expense> expense, ExpenseInsightsUpdateService expenseInsightsUpdateService) {
        this.expense = expense;
        this.expenseInsightsUpdateService = expenseInsightsUpdateService;
    }
}
