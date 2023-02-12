package com.example.budge.homework;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Getter
public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public int getOverlappingDays(Period another) {

        if (this.start.isAfter(another.getEnd()) || this.end.isBefore(another.getStart())) {
            return 0;
        }

        LocalDate periodStart = this.start.isAfter(another.getStart()) ? this.start : another.getStart();
        LocalDate periodEnd = this.end.isBefore(another.getEnd()) ? this.end : another.getEnd();

        return (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
    }
}
