package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BudgetCalculator {

    private final BudgetRepo budgetRepo;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Double query(LocalDate start, LocalDate end) {
        List<YearMonth> monthRange = getMonthRange(start, end);

        List<BudgetVo> budgetVos = new ArrayList<>();
        for (Budget budget : budgetRepo.getAll()) {
            BudgetVo vo = BudgetVo.builder()
                    .yearMonth(YearMonth.parse(budget.getYearMonth(), df))
                    .amount(budget.getAmount())
                    .build();
            if (monthRange.contains(vo.getYearMonth())) {
                budgetVos.add(vo);
            }
        }


        List<Double> priceUnitEachMonth = budgetVos
                .stream()
                .map(budgetVo -> {
                    return budgetVo.getAmount() / (double) (budgetVo.getYearMonth().lengthOfMonth());
                })
                .collect(toList());

        List<Integer> dayCountsEachMonth = new ArrayList<>();
        if (budgetVos.size() == 1) {
            int days = (int) ChronoUnit.DAYS.between(start, end.plusDays(1L));
            dayCountsEachMonth.add(days);
        } else {


            for (int i = 0; i < budgetVos.size(); i++) {
                BudgetVo budgetVo = budgetVos.get(i);
                YearMonth currentBudgetYearMonth = budgetVo.getYearMonth();

                LocalDate periodStart;
                LocalDate periodEnd;
                if (i == 0) {
                    periodStart = start;
                    periodEnd = currentBudgetYearMonth.atEndOfMonth();
                } else if (i == budgetVos.size() - 1) {
                    periodStart = currentBudgetYearMonth.atDay(1);
                    periodEnd = end;
                } else {
                    periodStart = LocalDate.of(currentBudgetYearMonth.getYear(), currentBudgetYearMonth.getMonth(), 1);
                    periodEnd = LocalDate.of(currentBudgetYearMonth.getYear(), currentBudgetYearMonth.getMonth(), currentBudgetYearMonth.lengthOfMonth());
                }
                int days = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
                dayCountsEachMonth.add(days);
            }
        }


        double rtn = 0.0;
        for (int i = 0; i < priceUnitEachMonth.size(); i++) {
            rtn += dayCountsEachMonth.get(i) * priceUnitEachMonth.get(i);
        }

        return rtn;
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
