package com.pocket.services.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.expense.dto.request.ExpenseDto;
import com.pocket.services.expense.service.ExpenseService;
import com.pocket.services.expense.utils.PageUtils;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addIncome(@Valid @RequestBody ExpenseDto expenseDto,
            @AuthenticationPrincipal UserInfo userInfo) {
        return expenseService.addExpense(expenseDto, userInfo);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getExpense(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(defaultValue = "desc") String order, @AuthenticationPrincipal UserInfo userInfo) {
        Pageable pageable = PageUtils.buildPageable(Math.max(0, page - 1), size, sort, order);
        return expenseService.getExpense(userInfo, pageable);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getExpenseById(@PathVariable Long id, @AuthenticationPrincipal UserInfo userInfo) {
        return expenseService.getExpenseById(id, userInfo);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseDto expenseDto,
            @AuthenticationPrincipal UserInfo userInfo) {
        return expenseService.updateExpense(id, expenseDto, userInfo);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id,
            @AuthenticationPrincipal UserInfo userInfo) throws Exception {
        return expenseService.deleteExpense(id, userInfo);
    }
}
