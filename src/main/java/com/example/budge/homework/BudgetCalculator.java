package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {

        double sum = 0.0;
        Period period = new Period(start, end);
        for (Budget budgetEntity : budgetRepo.getAll()) {

            sum += budgetEntity.toVo().getOverlappingAmount(period);
        }

        return sum;
    }

}
