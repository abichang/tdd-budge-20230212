package com.example.budge.homework;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Period {
    private final LocalDate start;
    private final LocalDate end;
}
