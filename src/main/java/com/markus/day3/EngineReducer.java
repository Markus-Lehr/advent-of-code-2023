package com.markus.day3;

import com.markus.Day;
import com.markus.util.Coordinate;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineReducer extends Day {

    public EngineReducer(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        return calculatePartSum(input);
    }

    @Override
    public Object solvePart2() {
        return calculateGearRatio(input);
    }
    private static final Pattern numberRegex = Pattern.compile("\\d+");
    private static final Pattern symbolRegex = Pattern.compile("[^\\d\\\\.]");
    private static final Pattern gearRegex = Pattern.compile("\\*");

    public BigInteger calculatePartSum(List<String> schematicLines) {
        List<NumberEntry> allEntries = new LinkedList<>();
        List<Coordinate> symbolCoords = new LinkedList<>();
        for (int i = 0; i < schematicLines.size(); i++) {
            String schematicLine = schematicLines.get(i);
            List<NumberEntry> numberEntries = numberEntriesForLine(schematicLine);
            int yCoord = i;
            numberEntries.forEach(entry -> entry.setLine(yCoord));
            allEntries.addAll(numberEntries);

            List<Coordinate> coordinates = symbolCoordsForLine(schematicLine);
            coordinates.forEach(coord -> coord.setY(yCoord));
            symbolCoords.addAll(coordinates);
        }

        return allEntries.stream().filter(entry ->
                        symbolCoords.stream().anyMatch(entry::nextTo)
                ).map(entry -> BigInteger.valueOf(entry.getNumber()))
                .reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    public BigInteger calculateGearRatio(List<String> schematicLines) {
        List<NumberEntry> allEntries = new LinkedList<>();
        List<Coordinate> gearCoords = new LinkedList<>();
        for (int i = 0; i < schematicLines.size(); i++) {
            String schematicLine = schematicLines.get(i);
            List<NumberEntry> numberEntries = numberEntriesForLine(schematicLine);
            int yCoord = i;
            numberEntries.forEach(entry -> entry.setLine(yCoord));
            allEntries.addAll(numberEntries);

            List<Coordinate> coordinates = gearCoordsForLine(schematicLine);
            coordinates.forEach(coord -> coord.setY(yCoord));
            gearCoords.addAll(coordinates);
        }

        return gearCoords.stream()
                .map(gearCoord -> calculateRatioForCoord(gearCoord, allEntries))
                .reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    private BigInteger calculateRatioForCoord(Coordinate gearCoord, List<NumberEntry> entries) {
        List<NumberEntry> list = entries.stream().filter(entry -> entry.nextTo(gearCoord)).toList();
        if (list.size() != 2) {
            return BigInteger.ZERO;
        }
        return BigInteger.valueOf(list.get(0).getNumber() * list.get(1).getNumber());
    }

    private List<NumberEntry> numberEntriesForLine(String line) {
        Matcher matcher = numberRegex.matcher(line);
        List<NumberEntry> result = new ArrayList<>();
        while (matcher.find()) {
            NumberEntry numberEntry = new NumberEntry();
            String stringNumber = matcher.group();
            numberEntry.setNumber(Long.parseLong(stringNumber));
            numberEntry.setStartIndex(matcher.start());
            numberEntry.setEndIndex(matcher.start() + stringNumber.length() - 1);
            result.add(numberEntry);
        }
        return result;
    }

    private List<Coordinate> symbolCoordsForLine(String line) {
        return coordsForLine(line, symbolRegex);
    }

    private List<Coordinate> gearCoordsForLine(String line) {
        return coordsForLine(line, gearRegex);
    }

    private List<Coordinate> coordsForLine(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        List<Coordinate> result = new ArrayList<>();
        while (matcher.find()) {
            Coordinate coord = new Coordinate();
            result.add(coord);
            coord.setX(matcher.start());
        }
        return result;
    }

    @Data
    private static class NumberEntry {
        private Long number;
        private int startIndex;
        private int endIndex;
        private int line;

        public boolean nextTo(Coordinate coord) {
            long yOffset = line - coord.getY();
            if (yOffset > 1 || yOffset < -1) {
                return false;
            }
            return coord.getX() >= startIndex - 1 && coord.getX() <= endIndex + 1;
        }
    }
}
