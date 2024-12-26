package com.pocket.services.income.exceptions;

public interface ErrorCode {
    String INCOME_CREATION_EXCEPTION = "INCOME_101";
    String INCOME_ALREADY_EXIST = "INCOME_102";
    String INCOME_CONSTRAINTS_DOES_NOT_MATCH = "INCOME_103";

    String INCOME_INVALID_PAGE = "INCOME_201";
    String INCOME_GET_EXCEPTION = "INCOME_202";
    
    String INCOME_NOT_FOUND = "INCOME_301";
    

    String INCOME_UPDATE_EXCEPTION = "INCOME_402";
    
}
