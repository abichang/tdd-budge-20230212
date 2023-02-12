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
        Period budgetPeriod = new Period(parseYearMonth().atDay(1), parseYearMonth().atEndOfMonth());
        return period.getOverlappingDays(budgetPeriod) * getDailyAmount();
    }

    private YearMonth parseYearMonth() {
        return this.yearMonth;
    }

    private int getDailyAmount() {
        return this.amount / parseYearMonth().lengthOfMonth();
    }
}
