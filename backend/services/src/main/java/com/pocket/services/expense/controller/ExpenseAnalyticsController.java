package com.pocket.services.expense.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.expense.service.ExpenseAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1/expense/analytics")
public class ExpenseAnalyticsController {

    @Autowired
    ExpenseAnalyticsService expenseAnalyticsService;

    @GetMapping(value = "/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnalyticsByYear(@PathVariable int year, @AuthenticationPrincipal UserInfo userInfo) {
        return expenseAnalyticsService.getExpenseAnalytics(year, userInfo);
    }

    @GetMapping(value = "/{month}/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnalyticsByMonth(@PathVariable int month, @PathVariable int year,
            @AuthenticationPrincipal UserInfo userInfo) {
        return expenseAnalyticsService.getExpenseAnalytics(month, year, userInfo);
    }

}
