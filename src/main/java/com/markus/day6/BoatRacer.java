package com.markus.day6;

import com.markus.Day;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoatRacer extends Day {
    public BoatRacer(List<String> input) {
        super(input);
    }

    @Override
    public BigInteger solvePart1() {
        return findWaysToWin(input);
    }

    @Override
    public Object solvePart2() {
        List<String> oneLargeRace = input.stream().map(line -> line.replaceAll(" ", "")).toList();
        return findWaysToWin(oneLargeRace);
    }

    public BigInteger findWaysToWin(List<String> input) {
        List<Race> races = racesFromInput(input);
        return races
                .stream()
                .map(Race::numbersOfWaysToBeatDistance)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::multiply).orElse(BigInteger.ZERO);
    }

    private List<Race> racesFromInput(List<String> input) {
        List<Race> result = new ArrayList<>();

        String timeLine = input.get(0).replace("Time:", "").strip();
        String distanceLine = input.get(1).replace("Distance:", "").strip();

        List<Long> times = numbersFromLine(timeLine);
        List<Long> distances = numbersFromLine(distanceLine);
        for (int i = 0; i < times.size(); i++) {
            result.add(new Race(times.get(i), distances.get(i)));
        }

        return result;
    }

    private List<Long> numbersFromLine(String line) {
        return Arrays
                .stream(line.split(" "))
                .filter(text -> !text.isBlank())
                .map(Long::parseLong)
                .toList();
    }
}
