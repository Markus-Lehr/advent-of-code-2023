package com.markus;

import com.markus.util.InputReader;

import java.util.List;

public class DayRunner {
    private final DayProducer[] producers;
    private final List<String>[] inputs;

    public DayRunner(DayProducer... producers) {
        this.producers = producers;
        inputs = new List[producers.length];
        loadInputs();
    }

    private void loadInputs() {
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = InputReader.getInput(i+1);
        }
    }

    public void runAll() {
        for (int i = 0; i < producers.length; i++) {
            runDay(i + 1);
        }
    }

    public void runLatest() {
        runDay(producers.length);
    }

    public void runDay(int day) {
        Day producedDay = producers[day - 1].produce(inputs[day - 1]);
        long start = System.currentTimeMillis();
        Object result = producedDay.solvePart1();
        long end = System.currentTimeMillis();
        System.out.println("Day " + day + " part 1: " + result + " took " + (end - start) + "ms");
        producedDay.solvePart2();

        start = System.currentTimeMillis();
        result = producedDay.solvePart2();
        end = System.currentTimeMillis();
        System.out.println("Day " + day + " part 2: " + result + " took " + (end - start) + "ms");
    }
}
