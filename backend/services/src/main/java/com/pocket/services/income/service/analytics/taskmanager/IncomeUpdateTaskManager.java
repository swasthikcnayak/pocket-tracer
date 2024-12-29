package com.pocket.services.income.service.analytics.taskmanager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.income.model.Income;
import com.pocket.services.income.service.analytics.service.IncomeInsightsUpdateService;

public class IncomeUpdateTaskManager extends AnalyticsTaskManager {

    Logger logger = LoggerFactory.getLogger(IncomeCreateTaskManager.class);

    public IncomeUpdateTaskManager(List<Income> expense, IncomeInsightsUpdateService incomeInsightsUpdateService) {
        super(expense, incomeInsightsUpdateService);
    }

    @Override
    public void run() {
        logger.info("INCOME UPDATED");
        incomeInsightsUpdateService.updateIncome(this.income);
    }
}
