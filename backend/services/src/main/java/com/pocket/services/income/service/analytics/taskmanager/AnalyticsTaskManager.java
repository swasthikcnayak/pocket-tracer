package com.pocket.services.income.service.analytics.taskmanager;

import java.util.List;

import com.pocket.services.income.model.Income;
import com.pocket.services.income.service.analytics.service.IncomeInsightsUpdateService;

public abstract class AnalyticsTaskManager implements Runnable {
    List<Income> income;
    IncomeInsightsUpdateService incomeInsightsUpdateService;

    public AnalyticsTaskManager(List<Income> income, IncomeInsightsUpdateService incomeInsightsUpdateService) {
        this.income = income;
        this.incomeInsightsUpdateService = incomeInsightsUpdateService;
    }
}
