package com.pocket.services.income.service.analytics.taskmanager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.income.model.Income;
import com.pocket.services.income.service.analytics.service.IncomeInsightsUpdateService;

public class IncomeCreateTaskManager extends AnalyticsTaskManager {
    Logger logger = LoggerFactory.getLogger(IncomeCreateTaskManager.class);

    public IncomeCreateTaskManager(List<Income> income, IncomeInsightsUpdateService incomeInsightsUpdateService) {
        super(income, incomeInsightsUpdateService);
    }

    @Override
    public void run() {
        logger.info("NEW INCOME ADDED");
        incomeInsightsUpdateService.addIncome(this.income.get(0));
    }

}
