package com.pocket.services.income.service.analytics.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocket.services.income.model.Income;
import com.pocket.services.income.repository.IncomeAnalyticsRepository;

@Service
public class IncomeInsightsUpdateService {

    Logger logger = LoggerFactory.getLogger(IncomeInsightsUpdateService.class);

    @Autowired
    IncomeAnalyticsRepository incomeAnalyticsRepository;

    @Transactional
    public void updateIncome(List<Income> income) {
        try {
            this.deleteIncome(income.get(0));
            this.addIncome(income.get(1));
        } catch (Exception e) {
            logger.error("Exception in update Income", e);
        }
    }

    @Transactional
    public void deleteIncome(Income income) {
        try {
            this.incomeAnalyticsRepository.deleteIncome(
                    income.getUser().getId(), income.getCategory().toString(), income.getDate().getMonthValue(),
                    income.getDate().getYear(), income.getAmount());
            logger.info("Income updated : Delete");
        } catch (Exception e) {
            logger.error("Exception in delete Income", e);
        }
    }

    @Transactional
    public void addIncome(Income income) {
        try {
            this.incomeAnalyticsRepository.addIncome(
                    income.getUser().getId(), income.getCategory().toString(), income.getDate().getMonthValue(),
                    income.getDate().getYear(), income.getAmount());
            logger.info("Income updated : Add");
        } catch (Exception e) {
            logger.error("Exception in add Income", e);
        }
    }
}
