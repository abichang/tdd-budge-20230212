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
        Period period = new Period(start, end);
        for (Budget budgetEntity : budgetRepo.getAll()) {
            BudgetVo budget = new BudgetVo(YearMonth.parse(budgetEntity.getYearMonth(), df), budgetEntity.getAmount());

            long overlappingAmount = budget.getOverlappingAmount(period);
            sum += overlappingAmount;
        }

        return sum;
    }

}
