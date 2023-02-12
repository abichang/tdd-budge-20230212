package com.example.budge.homework;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");

    private String yearMonth;

    private Integer amount;

    public long getOverlappingAmount(Period period) {
        YearMonth yearMonth1 = toVo().getYearMonth();
        int dailyAmount = toVo().getAmount() / yearMonth1.lengthOfMonth();
        return period.getOverlappingDays(new Period(yearMonth1.atDay(1), yearMonth1.atEndOfMonth())) * dailyAmount;
    }

    public BudgetVo toVo() {
        return new BudgetVo(YearMonth.parse(getYearMonth(), df), getAmount());
    }
}
