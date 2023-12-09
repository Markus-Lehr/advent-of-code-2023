package com.markus.day1;

import com.markus.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class StartEndReducer extends Day {
    private static final Map<String, Long> valuesWithText = new HashMap<>();
    private static final Set<String> keysWithText = valuesWithText.keySet();
    private static final Set<String> keys = Set.of(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    );

    static {
        valuesWithText.put("one", 1L);
        valuesWithText.put("two", 2L);
        valuesWithText.put("three", 3L);
        valuesWithText.put("four", 4L);
        valuesWithText.put("five", 5L);
        valuesWithText.put("six", 6L);
        valuesWithText.put("seven", 7L);
        valuesWithText.put("eight", 8L);
        valuesWithText.put("nine", 9L);
        valuesWithText.put("1", 1L);
        valuesWithText.put("2", 2L);
        valuesWithText.put("3", 3L);
        valuesWithText.put("4", 4L);
        valuesWithText.put("5", 5L);
        valuesWithText.put("6", 6L);
        valuesWithText.put("7", 7L);
        valuesWithText.put("8", 8L);
        valuesWithText.put("9", 9L);
    }

    public StartEndReducer(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        return reduceLines(input, line -> processLine(line, keys));
    }

    @Override
    public Object solvePart2() {
        return reduceLines(input, line -> processLine(line, keysWithText));
    }

    public Long reduceLines(List<String> lines, Function<String, Long> reduction) {

        return lines.stream()
                .parallel()
                .map(reduction)
                .reduce(Long::sum).orElse(0L);
    }

    private Long processLine(String line, Set<String> keys) {
        int firstIndex = Integer.MAX_VALUE;
        String firstString = "";
        int lastIndex = Integer.MIN_VALUE;
        String lastString = "";
        int index;
        for (String key : keys) {
            index = line.indexOf(key);
            if (index != -1 && index < firstIndex) {
                firstIndex = index;
                firstString = key;
            }
            index = line.lastIndexOf(key);
            if (index != -1 && index > lastIndex) {
                lastIndex = index;
                lastString = key;
            }
        }
        return valuesWithText.get(firstString) * 10 + valuesWithText.get(lastString);
    }
}
