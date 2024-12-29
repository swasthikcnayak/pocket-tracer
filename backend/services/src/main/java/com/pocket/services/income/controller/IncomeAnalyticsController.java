package com.pocket.services.income.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.income.service.IncomeAnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1/income/analytics")
public class IncomeAnalyticsController {

    @Autowired
    IncomeAnalyticsService incomeAnalyticsService;

    @GetMapping(value = "/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnalyticsByYear(@PathVariable int year, @AuthenticationPrincipal UserInfo userInfo) {
        return incomeAnalyticsService.getIncomeAnalytics(year, userInfo);
    }

    @GetMapping(value = "/{month}/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnalyticsByMonth(@PathVariable int month, @PathVariable int year,
            @AuthenticationPrincipal UserInfo userInfo) {
        return incomeAnalyticsService.getIncomeAnalytics(month, year, userInfo);
    }

}
