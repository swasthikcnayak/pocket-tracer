package com.pocket.services.expense.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.expense.dto.internal.Task;
import com.pocket.services.expense.dto.response.ExpenseAnalyticsByCategoryDto;
import com.pocket.services.expense.model.Expense;
import com.pocket.services.expense.repository.ExpenseAnalyticsRepository;
import com.pocket.services.expense.service.analytics.AnalyticsTaskManagerFactory;
import com.pocket.services.expense.service.analytics.taskmanager.AnalyticsTaskManager;

@Service
public class ExpenseAnalyticsService {

    Logger logger = LoggerFactory.getLogger(ExpenseAnalyticsService.class);

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    @Qualifier("expenseTaskManagerFactory")
    AnalyticsTaskManagerFactory analyticsTaskManagerFactory;

    @Autowired
    ExpenseAnalyticsRepository expenseAnalyticsRepository;

    public ResponseEntity<?> getExpenseAnalytics(int year, UserInfo userInfo) {
        List<ExpenseAnalyticsByCategoryDto> expenseAnalytics = expenseAnalyticsRepository
                .getAnalyticsForUserByYear(userInfo.getId(), year);
        return ResponseEntity.ok().body(expenseAnalytics);
    }

    public ResponseEntity<?> getExpenseAnalytics(int month, int year, UserInfo userInfo) {
        List<ExpenseAnalyticsByCategoryDto> expenseAnalytics = expenseAnalyticsRepository
                .getAnalyticsForUserByMonthAndYear(userInfo.getId(), month, year);
        return ResponseEntity.ok().body(expenseAnalytics);
    }

    public void handleExpense(Task task, List<Expense> expense) {
        AnalyticsTaskManager taskManager = analyticsTaskManagerFactory.getTaskManager(task, expense);
        threadPoolTaskExecutor.execute(taskManager);
    }

}
