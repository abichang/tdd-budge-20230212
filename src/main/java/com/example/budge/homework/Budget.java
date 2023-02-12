package com.example.budge.homework;

import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Getter
public class Budget {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    private final Period period;
    private final YearMonth yearMonth;
    private final Integer amount;

    public Budget(String yearMonth, Integer amount) {
        this.yearMonth = YearMonth.parse(yearMonth, df);
        this.amount = amount;
        this.period = new Period(getFirstDay(), getEndDay());
    }

    private LocalDate getFirstDay() {
        return this.yearMonth.atDay(1);
    }

    private LocalDate getEndDay() {
        return this.yearMonth.atEndOfMonth();
    }

    public long getOverlappingAmount(Period period) {
        return period.getOverlappingDays(this.period) * getDailyAmount();
    }

    private int getDailyAmount() {
        return this.amount / getTotalDays();
    }

    private int getTotalDays() {
        return this.yearMonth.lengthOfMonth();
    }
}
