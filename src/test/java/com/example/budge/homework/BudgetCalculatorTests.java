package com.example.budge.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class BudgetCalculatorTests {

    private final BudgetRepo budgetRepo = Mockito.mock(BudgetRepo.class);


    @Test
    void testSingleDayInSingleMonth() {
        List<Budget> budges = Arrays.asList(
                budget("202101", 31)
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 1);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo(1);
    }

    private static Budget budget(String yearMonth, int amount) {
        return new Budget(yearMonth, amount);
    }

    @Test
    void testMultiDayInSingleMonth() {
        List<Budget> budges = Arrays.asList(
                budget("202102", 56)
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 2, 1);
        LocalDate end = LocalDate.of(2021, 2, 20);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo(40);
    }

    @Test
    void testMultiDayInMultiMonth() {
        List<Budget> budges = Arrays.asList(
                budget("202102", 28),
                budget("202103", 31 * 2),
                budget("202104", 30 * 3)
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 2, 28);
        LocalDate end = LocalDate.of(2021, 4, 1);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo(66);
    }


    @Test
    void testMultiDayInMultiMonth2() {
        List<Budget> budges = Arrays.asList(
                budget("202102", 28),
                budget("202103", 31 * 2),
                budget("202104", 30 * 3),
                budget("202105", 31 * 4),
                budget("202106", 30 * 5)
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 2, 28);
        LocalDate end = LocalDate.of(2021, 4, 2);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo((1 * 1) + (31 * 2) + (2 * 3));
    }
}
