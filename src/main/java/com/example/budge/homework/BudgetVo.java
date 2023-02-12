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
        YearMonth yearMonthxxx = getYearMonth();
        return period.getOverlappingDays(new Period(yearMonthxxx.atDay(1), yearMonthxxx.atEndOfMonth())) * getDailyAmount();
    }

    private int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
