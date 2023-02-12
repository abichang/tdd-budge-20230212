package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BudgetCalculator {

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");
    private DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BudgetRepo budgetRepo;

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {
        List<String> monthRange = getMonthRange(start, end);

        List<BudgetVo> budgetVos = budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(Collectors.toList());

        List<Integer> dayCountsEachMonth = new ArrayList<>();
        if (budgetVos.size() == 1) {
            dayCountsEachMonth.add(end.getDayOfMonth() - start.getDayOfMonth() + 1);
        } else {
            for (int i = 0; i < budgetVos.size(); i++) {
                if (i == 0) {
                    dayCountsEachMonth.add(budgetVos.get(0).getYearMonth().lengthOfMonth() - start.getDayOfMonth() + 1);
                } else if (i == budgetVos.size() - 1) {
                    dayCountsEachMonth.add(end.getDayOfMonth());
                } else {
                    dayCountsEachMonth.add(budgetVos.get(i).getYearMonth().lengthOfMonth());
                }
            }
        }

        List<Double> priceUnitEachMonth = budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(toList())
                .stream()
                .map(v -> {
                    return v.getAmount() / (double) (v.getYearMonth().lengthOfMonth());
                })
                .collect(toList());

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
