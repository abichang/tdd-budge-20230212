package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {


        double rtn = 0.0;
        YearMonth startYearMonth = YearMonth.from(start);
        YearMonth endYearMonth = YearMonth.from(end);
        for (Budget budget : budgetRepo.getAll()) {

            YearMonth budgetYearMonth = YearMonth.parse(budget.getYearMonth(), df);
            double overlappingAmount = 0;

            if ((budgetYearMonth.equals(startYearMonth) || budgetYearMonth.isAfter(startYearMonth)) &&
                    (budgetYearMonth.equals(endYearMonth) || budgetYearMonth.isBefore(endYearMonth))) {

                LocalDate budgetEndDate = budgetYearMonth.atEndOfMonth();
                double dailyAmount = budget.getAmount() / (double) budgetEndDate.getDayOfMonth();


                LocalDate budgetStartDate = budgetYearMonth.atDay(1);
                int overlappingStartDay = startYearMonth.equals(budgetYearMonth) ? start.getDayOfMonth() : budgetStartDate.getDayOfMonth();
                int overlappingEndDay = endYearMonth.equals(budgetYearMonth) ? end.getDayOfMonth() : budgetEndDate.getDayOfMonth();
                int overlappingDays = overlappingEndDay - overlappingStartDay + 1;
                overlappingAmount = overlappingDays * dailyAmount;
            }

            rtn += overlappingAmount;
        }

        return rtn;
    }
}
