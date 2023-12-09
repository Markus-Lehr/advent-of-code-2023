package com.markus;

import java.util.List;
import java.util.Objects;

public abstract class Day {
    protected final List<String> input;

    public Day(List<String> input) {
        this.input = input;
    }

    public abstract Object solvePart1();
    public abstract Object solvePart2();
}
