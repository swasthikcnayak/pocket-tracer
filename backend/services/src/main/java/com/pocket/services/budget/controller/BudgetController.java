package com.pocket.services.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.budget.dto.request.BudgetDto;
import com.pocket.services.budget.service.BudgetService;
import com.pocket.services.common.security.dto.UserInfo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/budget")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBudget(@Valid @RequestBody BudgetDto budgetDto,
            @AuthenticationPrincipal UserInfo userInfo) {
        return budgetService.addBudget(budgetDto, userInfo);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBudget(@RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "2020") int year, @AuthenticationPrincipal UserInfo userInfo) throws Exception {
        return budgetService.getBudget(userInfo, month, year);
    }
}
