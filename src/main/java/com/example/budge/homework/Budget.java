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

    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    private String yearMonth;

    private Integer amount;

    public YearMonth getParsedYearMonth() {
        return YearMonth.parse(this.yearMonth, YEAR_MONTH_FORMATTER);
    }
}
