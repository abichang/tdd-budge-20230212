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
        return period.getOverlappingDays(new Period(this.yearMonth.atDay(1), this.yearMonth.atEndOfMonth())) * getDailyAmount();
    }

    private int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
