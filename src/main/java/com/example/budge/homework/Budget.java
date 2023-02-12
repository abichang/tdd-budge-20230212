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
        YearMonth yearMonth = parseYearMonth();
        int dailyAmount = this.amount / yearMonth.lengthOfMonth();
        return period.getOverlappingDays(new Period(yearMonth.atDay(1), yearMonth.atEndOfMonth())) * dailyAmount;
    }

    private YearMonth parseYearMonth() {
        return YearMonth.parse(this.yearMonth, df);
    }
}
