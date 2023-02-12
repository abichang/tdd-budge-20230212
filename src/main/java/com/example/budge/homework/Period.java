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
        LocalDate budgetStartDay = budget.getStartDay();
        LocalDate budgetEndDay = budget.getEndDay();
        
        if (this.start.isAfter(budgetEndDay) || this.end.isBefore(budgetStartDay)) {
            return 0;
        }

        LocalDate periodStart = this.start.isAfter(budgetStartDay) ? this.start : budgetStartDay;
        LocalDate periodEnd = this.end.isBefore(budgetEndDay) ? this.end : budgetEndDay;

        return (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
    }
}
