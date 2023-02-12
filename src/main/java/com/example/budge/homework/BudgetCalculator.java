package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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
            int days = period.getOverlappingDays(new Period(budget.getStartDay(), budget.getEndDay()));
            sum += days * budget.getDailyAmount();
        }

        return sum;
    }

}
