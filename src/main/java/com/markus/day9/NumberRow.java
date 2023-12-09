package com.markus.day9;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NumberRow {
    private final List<Long> numbers;
    private NumberRow childRow;

    public NumberRow(List<Long> numbers) {
        this.numbers = numbers;
    }

    public boolean isAllZeros() {
        return numbers.stream().allMatch(v -> v == 0L);
    }

    public void calculateChildRows() {
        if (isAllZeros()) {
            return;
        }
        List<Long> differences = new ArrayList<>(numbers.size() - 1);
        for (int i = 0; i < numbers.size() - 1; i++) {
            differences.add(numbers.get(i + 1) - numbers.get(i));
        }
        childRow = new NumberRow(differences);
        childRow.calculateChildRows();
    }

    public Long extrapolate() {
        if (isAllZeros()) {
            return 0L;
        }
        Long newTarget = childRow.extrapolate();
        Long lastNumber = numbers.get(numbers.size() - 1);
        Long newNumber = lastNumber + newTarget;
        numbers.add(newNumber);
        return newNumber;
    }

    public Long leftrapolate() {
        if (isAllZeros()) {
            return 0L;
        }
        Long newTarget = childRow.leftrapolate();
        Long firstNumber = numbers.get(0);
        Long newNumber = firstNumber - newTarget;
        numbers.add(0, newNumber);
        return newNumber;
    }

    @Override
    public String toString() {
        String childString = childRow == null ? "" : ("\n" + childRow.toString());
        return String.join(" ", numbers.stream().map(String::valueOf).toList()) + childString;
    }
}
