package com.pocket.services.expense.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExpenseSuccessReponse {
    int status;
    String message;
}
