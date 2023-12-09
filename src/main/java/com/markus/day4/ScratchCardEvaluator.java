package com.markus.day4;

import com.markus.Day;
import lombok.Data;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScratchCardEvaluator extends Day {
    public ScratchCardEvaluator(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        return evaluateScratchCards(input);
    }

    @Override
    public Object solvePart2() {
        return getNumberOfWinningScratchCards(input);
    }

    public BigInteger evaluateScratchCards(List<String> lines) {
        return lines.stream()
                .map(ScratchCard::new)
                .map(ScratchCard::getValue)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
    }

    public BigInteger getNumberOfWinningScratchCards(List<String> lines) {
        TreeMap<Long, ScratchCard> scratchCardMap = new TreeMap<>();
        lines.stream()
                .map(ScratchCard::new)
                .forEach(card -> scratchCardMap.put(card.id, card));
        scratchCardMap.forEach((id, card) -> {
            int matches = card.getMatches();
            if (matches > 0) {
                for (long i = id + 1L; i <= id + matches; i++) {
                    ScratchCard targetCard = scratchCardMap.get(i);
                    targetCard.setCopies(targetCard.copies + card.copies);
                }
            }
        });
        return scratchCardMap.values().stream().map(ScratchCard::getCopies).map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    private static Long twoPow(Long exp) {
        return Math.round(Math.pow(2, exp));
    }


    @Data
    public static class ScratchCard {
        private static final Pattern idPattern = Pattern.compile("Card\\s+(\\d+)");
        private Set<Long> winningNumbers = new HashSet<>();
        private Set<Long> actualNumbers = new HashSet<>();
        private Long id;
        private Long copies = 1L;

        public ScratchCard(String line) {
            String[] parts = line.split(":");
            setCardId(parts[0]);
            String[] numbers = parts[1].split("\\|");
            winningNumbers = numbersFromString(numbers[0]);
            actualNumbers = numbersFromString(numbers[1]);
        }

        public int getMatches() {
            return (int) actualNumbers.stream().filter(winningNumbers::contains).count();
        }

        public BigInteger getValue() {
            int exp = getMatches() - 1;
            if (exp == -1) {
                return BigInteger.ZERO;
            }
            return BigInteger.TWO.pow(exp);
        }

        private Set<Long> numbersFromString(String numberString) {
            return Arrays.stream(numberString.strip().split(" ")).filter(str -> !str.isBlank()).map(Long::parseLong).collect(Collectors.toSet());
        }

        private void setCardId(String part) {
            Matcher matcher = idPattern.matcher(part);
            matcher.find();
            id = Long.parseLong(matcher.group(1));
        }
    }
}
