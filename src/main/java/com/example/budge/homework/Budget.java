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

    private String yearMonth;

    private Integer amount;

    public BudgetVo toVo(DateTimeFormatter df) {
        return new BudgetVo(YearMonth.parse(getYearMonth(), df), getAmount());
    }
}
