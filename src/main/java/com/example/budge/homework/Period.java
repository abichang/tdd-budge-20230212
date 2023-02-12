package com.example.budge.homework;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Getter
public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public int getOverlappingDays(BudgetVo budget) {
        int days;
        if (this.start.isAfter(budget.getEndDay()) || this.end.isBefore(budget.getStartDay())) {
            days = 0;
        } else {
            LocalDate periodStart = this.start.isAfter(budget.getStartDay()) ? this.start : budget.getStartDay();
            LocalDate periodEnd = this.end.isBefore(budget.getEndDay()) ? this.end : budget.getEndDay();

            days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
        }
        return days;
    }
}
