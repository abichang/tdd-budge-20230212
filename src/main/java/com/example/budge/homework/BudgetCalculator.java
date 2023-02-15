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

        List<BudgetVo> filteredBudgets = new ArrayList<>();
        List<Double> priceUnitEachMonth = new ArrayList<>();
        List<Integer> dayCountsEachMonth = new ArrayList<>();
        for (Budget budget : budgetRepo.getAll()) {
            BudgetVo budgetVo = BudgetVo.builder()
                    .yearMonth(LocalDate.parse(budget.getYearMonth() + "01", df2))
                    .amount(budget.getAmount())
                    .build();
            if (monthRange.contains(df.format(budgetVo.getYearMonth()))) {
                filteredBudgets.add(budgetVo);

                Double dailyAmount = budgetVo.getAmount() / (double) (budgetVo.getYearMonth().lengthOfMonth());
                priceUnitEachMonth.add(dailyAmount);

                YearMonth startYearMonth = YearMonth.from(start);
                YearMonth endYearMonth = YearMonth.from(end);
                YearMonth budgetVoYearMonth = YearMonth.from(budgetVo.getYearMonth());

                int overlappingStartDay = startYearMonth.equals(budgetVoYearMonth) ? start.getDayOfMonth() : budgetVoYearMonth.atDay(1).getDayOfMonth();
                int overlappingEndDay = endYearMonth.equals(budgetVoYearMonth) ? end.getDayOfMonth() : budgetVo.getYearMonth().lengthOfMonth();
                dayCountsEachMonth.add(overlappingEndDay - overlappingStartDay + 1);
            }
        }


        double rtn = 0.0;
        for (int i = 0; i < priceUnitEachMonth.size(); i++) {
            rtn += dayCountsEachMonth.get(i) * priceUnitEachMonth.get(i);
        }

        return rtn;
    }
}
