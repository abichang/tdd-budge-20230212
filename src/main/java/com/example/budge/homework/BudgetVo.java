package com.example.budge.homework;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class BudgetVo {

    private final YearMonth yearMonth;
    private final Integer amount;
    private final Period period;

    public BudgetVo(YearMonth yearMonth, Integer amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
        this.period = new Period(this.yearMonth.atDay(1), this.yearMonth.atEndOfMonth());
    }

    public Period getPeriod() {
        return period;
    }

    public int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
