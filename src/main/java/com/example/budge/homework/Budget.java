package com.example.budge.homework;

import lombok.Data;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Data
public class Budget {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    private YearMonth yearMonth;

    private Integer amount;

    public Budget(String yearMonth, Integer amount) {
        this.yearMonth = YearMonth.parse(yearMonth, df);
        this.amount = amount;
    }

    public long getOverlappingAmount(Period period) {
        Period budgetPeriod = new Period(this.yearMonth.atDay(1), this.yearMonth.atEndOfMonth());
        return period.getOverlappingDays(budgetPeriod) * getDailyAmount();
    }

    private int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
