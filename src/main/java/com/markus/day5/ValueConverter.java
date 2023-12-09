package com.markus.day5;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@Data
public class ValueConverter {
    private String sourceCategory;
    private String targetCategory;
    private TreeMap<Long, ConverterRange> conversions = new TreeMap<>();
    private List<ConverterRange> bakedConversions;

    public ValueConverter(String declarationLine) {
        String[] parts = declarationLine.replace(" map:", "").strip().split("-to-");
        sourceCategory = parts[0];
        targetCategory = parts[1];
    }

    public void addConversion(String conversionLine) {
        List<Long> numbers = Arrays.stream(conversionLine.strip().split(" ")).map(Long::parseLong).toList();
        ConverterRange range = new ConverterRange(numbers.get(0), numbers.get(1), numbers.get(2));
        conversions.put(range.getSourceStart(), range);
    }

    public void bakeConversions() {
        bakedConversions = new ArrayList<>();
        Long current = 0L;
        for (ConverterRange range : conversions.values()) {
            if (range.sourceStart > current) {
                // create identity mapper to pad space
                bakedConversions.add(new ConverterRange(current, current, range.sourceStart - current));
            }
            bakedConversions.add(range);
            current = range.sourceStart + range.range;
        }
        bakedConversions.add(new ConverterRange(current, current, Long.MAX_VALUE - current - 1));
    }

    public List<SeedRange> convert(SeedRange seedRange) {
        if (seedRange.start > seedRange.end) {
            throw new IllegalStateException("range start must not be bigger than end");
        }
        ConverterRange startRange = null;
        int startIndex = -1;
        ConverterRange endRange = null;
        int endIndex = -1;
        for (int i = 0; i < bakedConversions.size(); i++) {
            ConverterRange bakedConversion = bakedConversions.get(i);
            if (bakedConversion.includes(seedRange.start)) {
                startRange = bakedConversion;
                startIndex = i;
            }
            if (bakedConversion.includes(seedRange.end)) {
                endRange = bakedConversion;
                endIndex = i;
            }
        }
        if (startRange == null || endRange == null || startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            throw new IllegalStateException("There must be a conversion range for the given seedRange " + seedRange);
        }

        if (startIndex == endIndex) {
            return List.of(new SeedRange(
                    startRange.map(seedRange.start),
                    endRange.map(seedRange.end))
            );
        }
        List<SeedRange> result = new ArrayList<>();
        result.add(
                new SeedRange(
                        startRange.map(seedRange.start),
                        startRange.map(startRange.getLastIncludedValue())
                )
        );
        result.addAll(bakedConversions.subList(startIndex + 1, endIndex)
                .stream()
                .sequential()
                .map(range -> new SeedRange(
                        range.map(range.sourceStart),
                        range.map(range.getLastIncludedValue())
                )).toList());
        result.add(
                new SeedRange(
                        endRange.map(endRange.sourceStart),
                        endRange.map(seedRange.end)
                )
        );
        return result;
    }

    @Data
    @AllArgsConstructor
    public static class SeedRange {
        private Long start;
        private Long end;

        public List<SeedRange> splitAt(Long startOfSecondRange) {
            return List.of(
                    new SeedRange(start, startOfSecondRange - 1),
                    new SeedRange(startOfSecondRange, end)
            );
        }
    }

    @Data
    private static class ConverterRange {
        private Long destinationStart;
        private Long sourceStart;
        private Long range;

        public ConverterRange(Long destinationStart, Long sourceStart, Long range) {
            this.destinationStart = destinationStart;
            this.sourceStart = sourceStart;
            this.range = range;
        }

        public boolean includes(Long value) {
            return value >= sourceStart && value < (sourceStart + range);
        }

        public Long map(Long value) {
            return value - sourceStart + destinationStart;
        }

        public Long getLastIncludedValue() {
            return sourceStart + range - 1;
        }

        public Long getFirstNotIncludedValue() {
            return sourceStart + range;
        }

        @Override
        public String toString() {
            return "Range[" + sourceStart + "-" + (sourceStart + range - 1) +
                    " -> " + destinationStart + "-" + (destinationStart + range - 1) +
                    ']';
        }
    }
}
