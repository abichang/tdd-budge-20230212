package com.example.budge.homework;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

@Builder(toBuilder = true)
@Data
public class BudgetVo {

    private YearMonth yearMonth;

    private Integer amount;

    public BudgetVo(YearMonth yearMonth, Integer amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public Period getPeriod() {
        return new Period(this.yearMonth.atDay(1), this.yearMonth.atEndOfMonth());
    }

    public int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
