package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BudgetCalculator {

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");
    private DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BudgetRepo budgetRepo;

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {
        YearMonth startY = YearMonth.from(start);

        LocalDate tmp = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
        List<String> monthRange = new ArrayList<>();
        while (!tmp.isAfter(end)) {
            monthRange.add(startY.format(df));
            tmp = tmp.plusMonths(1);
            startY = YearMonth.from(tmp);
        }

        List<BudgetVo> budgetVos = budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.toList());

        List<Integer> dayCounts = new ArrayList<>();
        if (budgetVos.size() == 1) {
            dayCounts.add(end.getDayOfMonth() - start.getDayOfMonth() + 1);
        } else {
            for (int i = 0; i < budgetVos.size(); i++) {
                if (i==0) {
                    dayCounts.add(budgetVos.get(0).getYearMonth().lengthOfMonth()-start.getDayOfMonth()+1);
                } else if (i == budgetVos.size() -1) {
                    dayCounts.add(end.getDayOfMonth());
                } else {
                    dayCounts.add(budgetVos.get(i).getYearMonth().lengthOfMonth());
                }
            }
        }

        List<Double> collect = budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(Collectors.toList())
                .stream()
                .map( v-> {
                    return v.getAmount()/ (double) (v.getYearMonth().lengthOfMonth());
                })
                .collect(Collectors.toList());

        double rtn = 0.0;
        for (int i = 0; i < collect.size(); i++) {
            rtn += dayCounts.get(i) * collect.get(i);
        }

        return rtn;
    }
}
