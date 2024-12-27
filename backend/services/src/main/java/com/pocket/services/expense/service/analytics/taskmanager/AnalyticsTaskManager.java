package com.pocket.services.expense.service.analytics.taskmanager;

import com.pocket.services.expense.model.Expense;

public abstract class AnalyticsTaskManager implements Runnable {
    Expense expense;

    public AnalyticsTaskManager(Expense expense) {
        this.expense = expense;
    }
}
