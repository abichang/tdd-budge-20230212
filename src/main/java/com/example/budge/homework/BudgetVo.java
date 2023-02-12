package com.example.budge.homework;

import lombok.Data;

import java.time.YearMonth;

@Data
public class BudgetVo {

    private YearMonth yearMonth;

    private Integer amount;
    private Period period;

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
