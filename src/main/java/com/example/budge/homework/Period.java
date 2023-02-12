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
        if (getStart().isAfter(budget.getEndDay()) || getEnd().isBefore(budget.getStartDay())) {
            days = 0;
        } else {
            LocalDate periodStart = getStart().isAfter(budget.getStartDay()) ? getStart() : budget.getStartDay();
            LocalDate periodEnd = getEnd().isBefore(budget.getEndDay()) ? getEnd() : budget.getEndDay();

            days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
        }
        return days;
    }
}
