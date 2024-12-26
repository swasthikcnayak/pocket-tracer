package com.pocket.services.expense.exceptions;

public interface ErrorCode {
    String EXPENSE_CREATION_EXCEPTION = "EXPENSE_101";
    String EXPENSE_ALREADY_EXIST = "EXPENSE_102";
    String EXPENSE_CONSTRAINTS_DOES_NOT_MATCH = "EXPENSE_103";

    String EXPENSE_INVALID_PAGE = "EXPENSE_201";
    String EXPENSE_GET_EXCEPTION = "EXPENSE_202";

    String EXPENSE_NOT_FOUND = "EXPENSE_301";

    String EXPENSE_UPDATE_EXCEPTION = "EXPENSE_402";
   
    
}
