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

    public long getOverlappingAmount(Period period) {
        YearMonth yearMonth = getYearMonth();
        int dailyAmount = this.amount / this.yearMonth.lengthOfMonth();
        return period.getOverlappingDays(new Period(yearMonth.atDay(1), yearMonth.atEndOfMonth())) * dailyAmount;
    }

}
