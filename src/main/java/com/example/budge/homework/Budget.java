package com.example.budge.homework;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Builder(toBuilder = true)
@Data
public class Budget {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    private String yearMonth;

    private Integer amount;

    public Budget(String yearMonth, Integer amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public long getOverlappingAmount(Period period) {
        Period budgetPeriod = new Period(parseYearMonth().atDay(1), parseYearMonth().atEndOfMonth());
        return period.getOverlappingDays(budgetPeriod) * getDailyAmount();
    }

    private YearMonth parseYearMonth() {
        return YearMonth.parse(this.yearMonth, df);
    }

    private int getDailyAmount() {
        return this.amount / parseYearMonth().lengthOfMonth();
    }
}
