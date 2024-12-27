package com.pocket.services.expense.service.analytics;

import org.springframework.stereotype.Component;

import com.pocket.services.expense.dto.internal.Task;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.service.analytics.taskmanager.AnalyticsTaskManager;
import com.pocket.services.expense.service.analytics.taskmanager.ExpenseCreateTaskManager;
import com.pocket.services.expense.service.analytics.taskmanager.ExpenseDeleteTaskManager;

@Component
public class AnalyticsTaskManagerFactory {

    public AnalyticsTaskManager getTaskManager(Expense expense,Task task) {
        switch (task) {
            case CREATE:
                return new ExpenseCreateTaskManager(expense);
            case DELETE:
                return new ExpenseDeleteTaskManager(expense);
            default:
                return null;
        }
    }
}
