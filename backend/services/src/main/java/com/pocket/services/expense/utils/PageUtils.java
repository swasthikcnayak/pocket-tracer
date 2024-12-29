package com.pocket.services.expense.utils;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.jsonwebtoken.lang.Arrays;

public class PageUtils {

    private static List<String> supportedSortFields = Arrays
            .asList(new String[] { "date", "id", "amount", "title", "category" });

    public static Pageable buildPageable(int page, int size, String sort, String order) {
        if (!isValidField(sort)) {
            return null;
        }
        Sort sortObject = Sort.by(sort);
        if (order.equals("asc"))
            sortObject = sortObject.ascending();
        else
            sortObject = sortObject.descending();
        return PageRequest.of(page, size, sortObject);
    }

    private static boolean isValidField(String sortField) {
        return supportedSortFields.contains(sortField);
    }
}
