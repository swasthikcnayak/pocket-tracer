package com.pocket.services.budget.exceptions;

public interface ErrorCode {
    String BUDGET_CREATION_EXCEPTION = "BUDGET_101";
    String BUDGET_ALREADY_EXIST = "BUDGET_102";
    String BUDGET_CONSTRAINTS_DOES_NOT_MATCH = "BUDGET_103";

    String BUDGET_INVALID_PAGE = "BUDGET_201";
    String BUDGET_GET_EXCEPTION = "BUDGET_202";

    String BUDGET_NOT_FOUND = "BUDGET_301";

    String BUDGET_UPDATE_EXCEPTION = "BUDGET_402";

}
