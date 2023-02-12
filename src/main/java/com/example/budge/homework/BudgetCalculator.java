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

        List<BudgetVo> budgetVos = makeBudgetVO(monthRange);

        List<Integer> dayCountsEachMonth = new ArrayList<>();
        if (budgetVos.size() == 1) {
            int daysDifferent = (int) ChronoUnit.DAYS.between(start, end.plusDays(1L));
            dayCountsEachMonth.add(daysDifferent);
        } else {
            for (int i = 0; i < budgetVos.size(); i++) {
                if (i == 0) {
                    int days = (int) ChronoUnit.DAYS.between(start, budgetVos.get(0).getYearMonth().withDayOfMonth(budgetVos.get(0).getYearMonth().lengthOfMonth()).plusDays(1L));
                    dayCountsEachMonth.add(days);
                } else if (i == budgetVos.size() - 1) {
                    int days = (int) ChronoUnit.DAYS.between(LocalDate.of(end.getYear(), end.getMonth(), 1), end.plusDays(1L));
                    dayCountsEachMonth.add(days);
                } else {
                    int days = (int) ChronoUnit.DAYS.between(LocalDate.of(budgetVos.get(i).getYearMonth().getYear(), budgetVos.get(i).getYearMonth().getMonth(), 1),
                            LocalDate.of(budgetVos.get(i).getYearMonth().getYear(), budgetVos.get(i).getYearMonth().getMonth(), budgetVos.get(i).getYearMonth().lengthOfMonth()).plusDays(1L));
                    dayCountsEachMonth.add(days);
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
                .map(budgetVo -> {
                    return budgetVo.getAmount() / (double) (budgetVo.getYearMonth().lengthOfMonth());
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

    private List<BudgetVo> makeBudgetVO(List<String> monthRange) {
        return budgetRepo.getAll().stream()
                .map(budget -> BudgetVo.builder()
                        .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                        .amount(budget.getAmount())
                        .build())
                .filter(budgetVo -> monthRange.contains(df.format(budgetVo.getYearMonth())))
                .collect(Collectors.toList());
    }
}
