package com.markus.day5;

import com.markus.Day;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class SeedMapper extends Day {

    public SeedMapper(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        ArrayList<String> inputCopy = new ArrayList<>();
        inputCopy.addAll(input);
        return findLowestLocation(inputCopy);
    }

    @Override
    public Object solvePart2() {
        ArrayList<String> inputCopy = new ArrayList<>();
        inputCopy.addAll(input);
        return findLowestLocationInRanges(inputCopy);
    }

    private Long findLowestLocation(List<String> input) {
        Map<String, List<ValueConverter.SeedRange>> values = new LinkedHashMap<>();
        values.put("seed", extractSlimSeedRanges(input.remove(0)));
        return findLowestLocation(input, values);
    }

    private Long findLowestLocationInRanges(List<String> input) {
        Map<String, List<ValueConverter.SeedRange>> values = new LinkedHashMap<>();
        values.put("seed", extractSeedRanges(input.remove(0)));
        return findLowestLocation(input, values);
    }

    private Long findLowestLocation(List<String> input, Map<String, List<ValueConverter.SeedRange>> ranges) {
        Map<String, ValueConverter> converters = extractConverters(input);
        List<ValueConverter.SeedRange> conversionResult =  applyConvertersToRanges(ranges, converters);
        for (Map.Entry<String, List<ValueConverter.SeedRange>> stringListEntry : ranges.entrySet()) {
            // System.out.println(stringListEntry.getKey() + ": " + stringListEntry.getValue());
        }
        return conversionResult
                .stream()
                .map(entry -> Math.max(entry.getStart(), entry.getEnd()))
                .min(Long::compare).orElse(0L);
    }

    private List<ValueConverter.SeedRange> applyConvertersToRanges(Map<String, List<ValueConverter.SeedRange>> values, Map<String, ValueConverter> converters) {
        String currentStage = values.keySet().stream().findFirst().orElseThrow();
        while (converters.containsKey(currentStage)) {
            List<ValueConverter.SeedRange> seedRanges = values.get(currentStage);
            ValueConverter converter = converters.get(currentStage);
            List<ValueConverter.SeedRange> result = seedRanges
                    .stream()
                    .parallel()
                    .map(converter::convert)
                    .flatMap(List::stream)
                    .toList();
            currentStage = converter.getTargetCategory();
            values.put(currentStage, result);
        }
        return values.get(currentStage);
    }

    private List<Long> extractSeedLine(String line) {
        line = line.replace("seeds: ", "").strip();
        return Arrays
                .stream(line.split(" "))
                .map(Long::parseLong)
                .toList();
    }

    private List<ValueConverter.SeedRange> extractSlimSeedRanges(String line) {
        List<Long> numbers = extractSeedLine(line);
        List<Long> paddedNumbers = new ArrayList<>(numbers.size() * 2);
        for (Long number : numbers) {
            paddedNumbers.add(number);
            paddedNumbers.add(1L);
        }
        return extractSeedRanges(paddedNumbers);
    }

    private List<ValueConverter.SeedRange> extractSeedRanges(String line) {
        List<Long> numbers = extractSeedLine(line);
        return extractSeedRanges(numbers);
    }

    public static List<ValueConverter.SeedRange> extractSeedRanges(Collection<Long> numbers) {
        List<ValueConverter.SeedRange> result = new ArrayList<>(numbers.size() / 2);
        Queue<Long> numberQueue = new ArrayBlockingQueue<Long>(numbers.size());
        numberQueue.addAll(numbers);
        while (!numberQueue.isEmpty()) {
            Long start = numberQueue.poll();
            Long range = numberQueue.poll();
            result.add(new ValueConverter.SeedRange(start, start + range - 1));
        }
        return result;
    }

    private Map<String, ValueConverter> extractConverters(List<String> lines) {
        Map<String, ValueConverter> result = new HashMap<>();
        ValueConverter current = null;
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.endsWith(":")) {
                if (current != null) {
                    current.bakeConversions();
                }
                current = new ValueConverter(line);
                result.put(current.getSourceCategory(), current);
            } else {
                current.addConversion(line);
            }
        }
        current.bakeConversions();
        return result;
    }
}
