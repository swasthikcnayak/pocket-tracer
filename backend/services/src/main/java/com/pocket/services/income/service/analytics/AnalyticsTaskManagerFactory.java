package com.pocket.services.income.service.analytics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pocket.services.income.dto.internal.Task;
import com.pocket.services.income.model.Income;
import com.pocket.services.income.repository.IncomeAnalyticsRepository;
import com.pocket.services.income.service.analytics.service.IncomeInsightsUpdateService;
import com.pocket.services.income.service.analytics.taskmanager.AnalyticsTaskManager;
import com.pocket.services.income.service.analytics.taskmanager.IncomeCreateTaskManager;
import com.pocket.services.income.service.analytics.taskmanager.IncomeDeleteTaskManager;
import com.pocket.services.income.service.analytics.taskmanager.IncomeUpdateTaskManager;

@Component("incomeTaskManagerFactory")
public class AnalyticsTaskManagerFactory {

    @Autowired
    IncomeAnalyticsRepository incomeAnalyticsRepository;

    @Autowired
    IncomeInsightsUpdateService incomeInsightsUpdateService;

    public AnalyticsTaskManager getTaskManager(Task task, List<Income> income) {
        switch (task) {
            case CREATE:
                return new IncomeCreateTaskManager(income, incomeInsightsUpdateService);
            case DELETE:
                return new IncomeDeleteTaskManager(income, incomeInsightsUpdateService);
            case UPDATE:
                return new IncomeUpdateTaskManager(income, incomeInsightsUpdateService);
            default:
                return null;
        }
    }
}
