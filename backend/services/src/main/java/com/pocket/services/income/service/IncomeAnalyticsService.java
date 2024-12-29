package com.pocket.services.income.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.income.dto.internal.Task;
import com.pocket.services.income.model.Income;
import com.pocket.services.income.service.analytics.AnalyticsTaskManagerFactory;
import com.pocket.services.income.service.analytics.taskmanager.AnalyticsTaskManager;
import com.pocket.services.income.dto.response.IncomeAnalyticsByCategoryDto;
import com.pocket.services.income.repository.IncomeAnalyticsRepository;

@Service
public class IncomeAnalyticsService {

    Logger logger = LoggerFactory.getLogger(IncomeAnalyticsService.class);

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    @Qualifier("incomeTaskManagerFactory")
    AnalyticsTaskManagerFactory analyticsTaskManagerFactory;

    @Autowired
    IncomeAnalyticsRepository incomeAnalyticsRepository;

    public ResponseEntity<?> getIncomeAnalytics(int year, UserInfo userInfo) {
        List<IncomeAnalyticsByCategoryDto> expenseAnalytics = incomeAnalyticsRepository
                .getAnalyticsForUserByYear(userInfo.getId(), year);
        return ResponseEntity.ok().body(expenseAnalytics);
    }

    public ResponseEntity<?> getIncomeAnalytics(int month, int year, UserInfo userInfo) {
        List<IncomeAnalyticsByCategoryDto> expenseAnalytics = incomeAnalyticsRepository
                .getAnalyticsForUserByMonthAndYear(userInfo.getId(), month, year);
        return ResponseEntity.ok().body(expenseAnalytics);
    }

    public void handleIncome(Task task, List<Income> income) {
        AnalyticsTaskManager taskManager = analyticsTaskManagerFactory.getTaskManager(task, income);
        threadPoolTaskExecutor.execute(taskManager);
    }

}
