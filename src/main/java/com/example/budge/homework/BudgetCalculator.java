package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");
    private DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {
        List<String> monthRange = getMonthRange(start, end);

        List<BudgetVo> budgetVos = budgetRepo.getAll().stream()
                .map(budget1 -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget1.getYearMonth() + "01", df2))
                        .amount(budget1.getAmount())
                        .build())
                .filter(budgetVo1 -> monthRange.contains(df.format(budgetVo1.getYearMonth())))
                .collect(Collectors.toList());

        List<Double> priceUnitEachMonth = budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(toList())
                .stream()
                .map(budgetVo -> {
                    return budgetVo.getAmount() / (double) (budgetVo.getYearMonth().lengthOfMonth());
                })
                .collect(toList());

        List<Integer> dayCountsEachMonth = new ArrayList<>();
        if (budgetVos.size() == 1) {
            int daysDifferent = (int) ChronoUnit.DAYS.between(start, end.plusDays(1L));
            dayCountsEachMonth.add(daysDifferent);
        } else {
            for (int i = 0; i < budgetVos.size(); i++) {
                LocalDate currentBudgetYearMonth = budgetVos.get(i).getYearMonth();

                LocalDate periodStart;
                LocalDate periodEnd;
                if (i == 0) {
                    periodStart = start;
                    periodEnd = currentBudgetYearMonth.withDayOfMonth(currentBudgetYearMonth.lengthOfMonth()).plusDays(1L);
                } else if (i == budgetVos.size() - 1) {
                    periodStart = LocalDate.of(end.getYear(), end.getMonth(), 1);
                    periodEnd = end.plusDays(1L);
                } else {
                    periodStart = LocalDate.of(currentBudgetYearMonth.getYear(), currentBudgetYearMonth.getMonth(), 1);
                    periodEnd = LocalDate.of(currentBudgetYearMonth.getYear(), currentBudgetYearMonth.getMonth(), currentBudgetYearMonth.lengthOfMonth()).plusDays(1L);
                }
                int days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd);
                dayCountsEachMonth.add(days);
            }
        }


        double rtn = 0.0;
        for (int i = 0; i < priceUnitEachMonth.size(); i++) {
            rtn += dayCountsEachMonth.get(i) * priceUnitEachMonth.get(i);
        }

        return rtn;
    }

    private List<String> getMonthRange(LocalDate start, LocalDate end) {
        YearMonth startY = YearMonth.from(start);

        // get iterator months
        // (202101, 202103) -> (01, 02, 03)
        LocalDate tmp = start.with(ChronoField.DAY_OF_MONTH, 1);
        List<String> monthRange = new ArrayList<>();
        while (!tmp.isAfter(end)) {
            monthRange.add(startY.format(df));
            tmp = tmp.plusMonths(1);
            startY = YearMonth.from(tmp);
        }
        return monthRange;
    }

}
