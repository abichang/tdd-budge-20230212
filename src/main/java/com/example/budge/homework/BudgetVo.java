package com.example.budge.homework;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetVo {

    private YearMonth yearMonth;

    private Integer amount;

    public Period getPeriod() {
        return new Period(this.yearMonth.atDay(1), this.yearMonth.atEndOfMonth());
    }

    public int getDailyAmount() {
        return this.amount / this.yearMonth.lengthOfMonth();
    }
}
