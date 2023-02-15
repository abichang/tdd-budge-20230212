package com.example.budge.homework;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        // get iterator months
        // (202101, 202103) -> (01, 02, 03)
        LocalDate tmp = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
        List<String> monthRange = new ArrayList<>();
        while (!tmp.isAfter(end)) {
            monthRange.add(startY.format(df));
            tmp = tmp.plusMonths(1);
            startY = YearMonth.from(tmp);
        }


        double rtn = 0.0;
        for (Budget budget : budgetRepo.getAll()) {
            BudgetVo budgetVo = BudgetVo.builder()
                    .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                    .amount(budget.getAmount())
                    .build();
            if (monthRange.contains(budget.getYearMonth())) {

                YearMonth budgetYearMonth = YearMonth.parse(budget.getYearMonth(), df);
                double dailyAmount = budget.getAmount() / (double) budgetYearMonth.lengthOfMonth();

                YearMonth startYearMonth = YearMonth.from(start);
                YearMonth endYearMonth = YearMonth.from(end);

                int overlappingStartDay = startYearMonth.equals(budgetYearMonth) ? start.getDayOfMonth() : budgetYearMonth.atDay(1).getDayOfMonth();
                int overlappingEndDay = endYearMonth.equals(budgetYearMonth) ? end.getDayOfMonth() : budgetVo.getYearMonth().lengthOfMonth();
                int overlappingDays = overlappingEndDay - overlappingStartDay + 1;
                rtn += overlappingDays * dailyAmount;
            }

        }

        return rtn;
    }
}
