package com.pocket.services.expense.service;

import java.util.List;

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

    public void handleExpense(Task task, List<Expense> expense) {
        AnalyticsTaskManager taskManager = analyticsTaskManagerFactory.getTaskManager(task, expense);
        threadPoolTaskExecutor.execute(taskManager);
    }

}
