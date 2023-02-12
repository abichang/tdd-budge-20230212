package com.example.budge.homework;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class BudgetVo {

    private final YearMonth yearMonth;
    private final Integer amount;

    public BudgetVo(YearMonth yearMonth, Integer amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

}
