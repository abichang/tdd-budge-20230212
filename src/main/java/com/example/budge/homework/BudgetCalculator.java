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

            if ((budgetYearMonth.equals(startYearMonth) || budgetYearMonth.isAfter(startYearMonth)) &&
                    (budgetYearMonth.equals(endYearMonth) || budgetYearMonth.isBefore(endYearMonth))) {

                double dailyAmount = budget.getAmount() / (double) budgetYearMonth.lengthOfMonth();


                LocalDate budgetStartDate = budgetYearMonth.atDay(1);
                int overlappingStartDay = startYearMonth.equals(budgetYearMonth) ? start.getDayOfMonth() : budgetStartDate.getDayOfMonth();
                int overlappingEndDay = endYearMonth.equals(budgetYearMonth) ? end.getDayOfMonth() : budgetYearMonth.lengthOfMonth();
                int overlappingDays = overlappingEndDay - overlappingStartDay + 1;
                rtn += overlappingDays * dailyAmount;
            }

        }

        return rtn;
    }
}
