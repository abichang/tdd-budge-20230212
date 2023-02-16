package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {


        double rtn = 0.0;

        for (Budget budget : budgetRepo.getAll()) {

            YearMonth budgetYearMonth = getParsedYearMonth(budget);

            LocalDate budgetStartDate = budgetYearMonth.atDay(1);
            LocalDate budgetEndDate = budgetYearMonth.atEndOfMonth();

            double overlappingAmount;

            if (end.isBefore(budgetStartDate) || start.isAfter(budgetEndDate)) {
                overlappingAmount = 0;
            } else {

                double dailyAmount = budget.getAmount() / (double) budgetEndDate.getDayOfMonth();


                int overlappingStartDay = start.isAfter(budgetStartDate) ? start.getDayOfMonth() : budgetStartDate.getDayOfMonth();
                int overlappingEndDay = end.isBefore(budgetEndDate) ? end.getDayOfMonth() : budgetEndDate.getDayOfMonth();
                int overlappingDays = overlappingEndDay - overlappingStartDay + 1;
                overlappingAmount = overlappingDays * dailyAmount;
            }

            rtn += overlappingAmount;
        }

        return rtn;
    }

    private YearMonth getParsedYearMonth(Budget budget) {
        return YearMonth.parse(budget.getYearMonth(), DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
