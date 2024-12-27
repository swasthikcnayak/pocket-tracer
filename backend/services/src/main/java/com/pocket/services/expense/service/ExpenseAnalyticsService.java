package com.pocket.services.expense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.pocket.services.expense.dto.internal.Task;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.service.analytics.AnalyticsTaskManagerFactory;
import com.pocket.services.expense.service.analytics.taskmanager.AnalyticsTaskManager;

@Service
public class ExpenseAnalyticsService {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    AnalyticsTaskManagerFactory analyticsTaskManagerFactory;

    public void handleExpense(Expense expense, Task task) {
        AnalyticsTaskManager taskManager = analyticsTaskManagerFactory.getTaskManager(expense,task);
        threadPoolTaskExecutor.execute(taskManager);
    }

}
