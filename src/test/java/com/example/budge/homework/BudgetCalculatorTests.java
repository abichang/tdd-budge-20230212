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
                Budget.builder().yearMonth("202101").amount(31).build()
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 1);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo(1);
    }

    @Test
    void testMultiDayInSingleMonth() {
        List<Budget> budges = Arrays.asList(
                Budget.builder().yearMonth("202102").amount(56).build()
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
                Budget.builder().yearMonth("202102").amount(28).build(),
                Budget.builder().yearMonth("202103").amount(31 * 2).build(),
                Budget.builder().yearMonth("202104").amount(30 * 3).build()
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
                Budget.builder().yearMonth("202102").amount(28).build(),
                Budget.builder().yearMonth("202103").amount(31 * 2).build(),
                Budget.builder().yearMonth("202104").amount(30 * 3).build(),
                Budget.builder().yearMonth("202105").amount(31 * 4).build(),
                Budget.builder().yearMonth("202106").amount(30 * 5).build()
        );
        when(budgetRepo.getAll()).thenReturn(budges);

        BudgetCalculator budgetCalculator = new BudgetCalculator(budgetRepo);
        LocalDate start = LocalDate.of(2021, 2, 28);
        LocalDate end = LocalDate.of(2021, 4, 2);

        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo((1 * 1) + (31 * 2) + (2 * 3));
    }
}
