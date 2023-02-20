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


        double rtn = 0.0;

        Period period = new Period(start, end);
        for (Budget budget : budgetRepo.getAll()) {

            double overlappingAmount = getOverlappingAmount(period, budget);

            rtn += overlappingAmount;
        }

        return rtn;
    }

    private static double getOverlappingAmount(Period period, Budget budget) {
        LocalDate budgetStartDate = budget.getStartDate();
        LocalDate budgetEndDate = budget.getEndDate();

        double overlappingAmount;

        if (!period.getEnd().isBefore(budgetStartDate) && !period.getStart().isAfter(budgetEndDate)) {

            double dailyAmount = budget.getAmount() / (double) budgetEndDate.getDayOfMonth();


            int overlappingStartDay = period.getStart().isAfter(budgetStartDate) ? period.getStart().getDayOfMonth() : budgetStartDate.getDayOfMonth();
            int overlappingEndDay = period.getEnd().isBefore(budgetEndDate) ? period.getEnd().getDayOfMonth() : budgetEndDate.getDayOfMonth();
            int overlappingDays = overlappingEndDay - overlappingStartDay + 1;
            overlappingAmount = overlappingDays * dailyAmount;
        } else {
            overlappingAmount = 0;
        }
        return overlappingAmount;
    }

}
