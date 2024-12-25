package com.pocket.services.budget.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BudgetSuccessReponse {
    int status;
    String message;
}
