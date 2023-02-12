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
        return toVo().getOverlappingAmount(period, toVo().getYearMonth());
    }

    public BudgetVo toVo() {
        return new BudgetVo(YearMonth.parse(getYearMonth(), df), getAmount());
    }
}
