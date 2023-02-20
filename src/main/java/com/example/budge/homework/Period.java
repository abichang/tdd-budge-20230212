package com.example.budge.homework;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public boolean isOverlapping(Period other) {
        return !this.end.isBefore(other.getStart()) && !this.start.isAfter(other.getEnd());
    }
}
