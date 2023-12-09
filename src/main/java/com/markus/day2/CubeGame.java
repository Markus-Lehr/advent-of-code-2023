package com.markus.day2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubeGame {
    private long id;
    private List<CubeSet> sets;
    private static final Pattern idPattern = Pattern.compile("Game (\\d+)");

    public CubeGame(String line) {
        String[] parts = line.split(":");
        initId(parts[0]);
        initSets(parts[1].split(";"));
    }

    private void initSets(String[] setStrings) {
        sets = Arrays.stream(setStrings).map(CubeSet::new).toList();
    }

    private void initId(String part) {
        Matcher matcher = idPattern.matcher(part);
        matcher.find();
        id = Long.parseLong(matcher.group(1));
    }

    public boolean satisFiesConstraint(Map<SetColor, Long> constraints) {
        return sets.stream().allMatch(set -> set.satisFiesConstraint(constraints));
    }

    public Long getPower() {
        Long requiredRed = 0L;
        Long requiredGreen = 0L;
        Long requiredBlue = 0L;
        for (CubeSet set : sets) {
            requiredRed = Math.max(requiredRed, set.colorCounts.getOrDefault(SetColor.RED, 0L));
            requiredGreen = Math.max(requiredGreen, set.colorCounts.getOrDefault(SetColor.GREEN, 0L));
            requiredBlue = Math.max(requiredBlue, set.colorCounts.getOrDefault(SetColor.BLUE, 0L));
        }
        return requiredRed * requiredGreen * requiredBlue;
    }

    public long getId() {
        return id;
    }

    public enum SetColor {
        RED("red"),
        GREEN("green"),
        BLUE("blue");

        private final String color;
        private final Pattern pattern;


        SetColor(String color) {
            this.color = color;
            pattern = Pattern.compile("(\\d+) " + color);
        }

        public String getColor() {
            return color;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public static class CubeSet {
        private final Map<SetColor, Long> colorCounts = new HashMap<>();
        public CubeSet(String line) {
            try {
                for (SetColor value : SetColor.values()) {
                    Matcher matcher = value.getPattern().matcher(line);
                    while (matcher.find()) {
                        colorCounts.put(value, Long.parseLong(matcher.group(1)));
                    }
                }
            } catch (IllegalStateException e) {
                throw new RuntimeException(e);
            }
        }

        public Map<SetColor, Long> getColorCounts() {
            return colorCounts;
        }

        public boolean satisFiesConstraint(Map<SetColor, Long> constraints) {
            for (SetColor setColor : colorCounts.keySet()) {
                if (colorCounts.get(setColor) > constraints.get(setColor)) {
                    return false;
                }
            }
            return true;
        }
    }
}
