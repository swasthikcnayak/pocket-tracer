package com.pocket.services.expense.service.analytics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pocket.services.expense.dto.internal.Task;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.service.analytics.service.ExpenseInsightsUpdateService;
import com.pocket.services.expense.service.analytics.taskmanager.AnalyticsTaskManager;
import com.pocket.services.expense.service.analytics.taskmanager.ExpenseCreateTaskManager;
import com.pocket.services.expense.service.analytics.taskmanager.ExpenseDeleteTaskManager;
import com.pocket.services.expense.service.analytics.taskmanager.ExpenseUpdateTaskManager;
import com.pocket.services.expense.repository.ExpenseAnalyticsRepository;

@Component("expenseTaskManagerFactory")
public class AnalyticsTaskManagerFactory {

    @Autowired
    ExpenseAnalyticsRepository expenseAnalyticsRepository;

    @Autowired
    ExpenseInsightsUpdateService expenseInsightsUpdateService;

    public AnalyticsTaskManager getTaskManager(Task task, List<Expense> expense) {
        switch (task) {
            case CREATE:
                return new ExpenseCreateTaskManager(expense, expenseInsightsUpdateService);
            case DELETE:
                return new ExpenseDeleteTaskManager(expense, expenseInsightsUpdateService);
            case UPDATE:
                return new ExpenseUpdateTaskManager(expense, expenseInsightsUpdateService);
            default:
                return null;
        }
    }
}
