package com.markus.day8;

import lombok.Data;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipError;

@Data
public class CamelMap {
    private static final Pattern destinationPattern = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");
    private final Map<String, Map<Direction, String>> destinations = new HashMap<>();
    private CyclicList<Direction> directions;

    public CamelMap(String directionInput, List<String> destinationInput) {
        ArrayList<Character> directionCharacters = new ArrayList<>(directionInput.length());
        for (char c : directionInput.toCharArray()) {
            directionCharacters.add(c);
        }
        directions = new CyclicList<>(
                directionCharacters
                        .stream()
                        .map(Direction::fromLetter)
                        .toList()
        );
        initDestinations(destinationInput);
    }

    public BigInteger traverse(String start, String end) {
        BigInteger counter = BigInteger.ZERO;
        String currentLocation = start;
        for (Direction direction : directions) {
            if (end.equals(currentLocation)) {
                break;
            }
            counter = counter.add(BigInteger.ONE);
            currentLocation = advance(currentLocation, direction);
        }

        return counter;
    }

    public BigInteger traverse(String start) {
        BigInteger counter = BigInteger.ZERO;
        String currentLocation = start;
        for (Direction direction : directions) {
            if (isEndNode(currentLocation)) {
                break;
            }
            counter = counter.add(BigInteger.ONE);
            currentLocation = advance(currentLocation, direction);
        }

        return counter;
    }

    public Map<BigInteger, Integer> primeFactors(BigInteger number) {
        Map<BigInteger, Integer> result = new HashMap<>();
        for (BigInteger factor = BigInteger.TWO; factor.compareTo(number.divide(factor)) <= 0; factor = factor.add(BigInteger.ONE)) {
            while (number.mod(factor).equals(BigInteger.ZERO)) {
                Integer oldCount = result.getOrDefault(factor, 0);
                result.put(factor, ++oldCount);
                number = number.divide(factor);
            }
        }
        if (number.compareTo(BigInteger.ONE) > 0) {
            Integer oldCount = result.getOrDefault(number, 0);
            result.put(number, ++oldCount);
        }
        return result;
    }

    public BigInteger lowestCommonMultiple(Collection<BigInteger> numbers) {
        Map<BigInteger, Integer> factors = new HashMap<>();
        List<Map<BigInteger, Integer>> factorizations = numbers.stream().map(this::primeFactors).toList();
        for (Map<BigInteger, Integer> factorization : factorizations) {
            for (Map.Entry<BigInteger, Integer> factorCount : factorization.entrySet()) {
                Integer oldFactor = factors.getOrDefault(factorCount.getKey(), 0);
                factors.put(factorCount.getKey(), Math.max(oldFactor, factorCount.getValue()));
            }
        }
        return factors.entrySet()
                .stream()
                .map(entry -> entry.getKey().multiply(BigInteger.valueOf(entry.getValue())))
                .reduce(BigInteger::multiply).orElse(BigInteger.ONE);
    }

    public BigInteger traverse() {
        Collection<String> nodesToAdvance = destinations.keySet()
                .stream()
                .filter(this::isStartNode)
                .collect(Collectors.toSet());
        Map<String, BigInteger> stepsToTargetForStart = new HashMap<>();
        nodesToAdvance
                .stream()
                .parallel()
                .forEach(node -> stepsToTargetForStart.put(node, traverse(node)));
        return lowestCommonMultiple(stepsToTargetForStart.values());
    }

    private boolean isEndNode(String location) {
        return location.endsWith("Z");
    }

    private boolean isStartNode(String location) {
        return location.endsWith("A");
    }

    private String advance(String location, Direction direction) {
        return destinations.get(location).get(direction);
    }

    private void initDestinations(List<String> destinationInput) {
        for (String line : destinationInput) {
            Matcher matcher = destinationPattern.matcher(line);
            if (!matcher.find()) {
                continue;
            }
            EnumMap<Direction, String> enumMap = new EnumMap<>(Direction.class);
            enumMap.put(Direction.LEFT, matcher.group(2));
            enumMap.put(Direction.RIGHT, matcher.group(3));
            destinations.put(matcher.group(1), enumMap);
        }
    }
}
