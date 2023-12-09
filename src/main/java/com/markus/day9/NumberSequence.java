package com.markus.day9;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class NumberSequence {
    private NumberRow startingRow;

    public NumberSequence(String line) {
        List<Long> numbers = new ArrayList<>(Arrays.stream(line.split(" ")).map(Long::parseLong).toList());
        startingRow = new NumberRow(numbers);
    }

    public Long extrapolate() {
        startingRow.calculateChildRows();
        return startingRow.extrapolate();
    }

    public Long leftrapolate() {
        startingRow.calculateChildRows();
        return startingRow.leftrapolate();
    }

    @Override
    public String toString() {
        return startingRow.toString();
    }
}
