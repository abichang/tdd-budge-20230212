package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {
        List<YearMonth> monthRange = getMonthRange(start, end);

        double sum = 0.0;
        for (Budget budgetEntity : budgetRepo.getAll()) {
            BudgetVo budget = BudgetVo.builder()
                    .yearMonth(YearMonth.parse(budgetEntity.getYearMonth(), df))
                    .amount(budgetEntity.getAmount())
                    .build();
            if (monthRange.contains(budget.getYearMonth())) {
                LocalDate periodStart = start.isAfter(budget.getStartDay()) ? start : budget.getStartDay();
                LocalDate periodEnd = end.isBefore(budget.getEndDay()) ? end : budget.getEndDay();

                double dailyAmount = budget.getDailyAmount();
                int days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
                sum += days * dailyAmount;
            }
        }

        return sum;
    }

    private List<YearMonth> getMonthRange(LocalDate start, LocalDate end) {

        YearMonth endYearMonth = YearMonth.from(end);

        List<YearMonth> results = new ArrayList<>();
        for (YearMonth current = YearMonth.from(start);
             current.isBefore(endYearMonth) || current.equals(endYearMonth);
             current = current.plusMonths(1L)) {
            results.add(current);
        }
        return results;
    }

}
