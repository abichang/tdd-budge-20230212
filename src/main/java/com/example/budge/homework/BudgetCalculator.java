package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {

        double sum = 0.0;
        for (Budget budgetEntity : budgetRepo.getAll()) {
            BudgetVo budget = BudgetVo.builder()
                    .yearMonth(YearMonth.parse(budgetEntity.getYearMonth(), df))
                    .amount(budgetEntity.getAmount())
                    .build();
            Period period = new Period(start, end);
            double overlappingAmount;
            int days;
            if (period.getStart().isAfter(budget.getEndDay()) || period.getEnd().isBefore(budget.getStartDay())) {
                days = 0;
                overlappingAmount = days * budget.getDailyAmount();
            } else {
                LocalDate periodStart = period.getStart().isAfter(budget.getStartDay()) ? period.getStart() : budget.getStartDay();
                LocalDate periodEnd = period.getEnd().isBefore(budget.getEndDay()) ? period.getEnd() : budget.getEndDay();

                days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
                overlappingAmount = days * budget.getDailyAmount();
            }
            sum += overlappingAmount;
        }

        return sum;
    }

}
