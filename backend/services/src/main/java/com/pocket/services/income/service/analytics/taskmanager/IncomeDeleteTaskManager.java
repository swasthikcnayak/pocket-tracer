package com.pocket.services.income.service.analytics.taskmanager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pocket.services.income.model.Income;
import com.pocket.services.income.service.analytics.service.IncomeInsightsUpdateService;

public class IncomeDeleteTaskManager extends AnalyticsTaskManager {
    Logger logger = LoggerFactory.getLogger(IncomeDeleteTaskManager.class);

    public IncomeDeleteTaskManager(List<Income> income, IncomeInsightsUpdateService incomeInsightsUpdateService) {
        super(income, incomeInsightsUpdateService);
    }

    @Override
    public void run() {
        logger.info("INCOME DELETED");
        incomeInsightsUpdateService.deleteIncome(this.income.get(0));
    }

}
