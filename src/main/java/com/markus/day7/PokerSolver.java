package com.markus.day7;

import com.markus.Day;

import java.math.BigInteger;
import java.util.List;

public class PokerSolver extends Day {

    public PokerSolver(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        List<PokerHand> sortedHands = handsFromInput(input, false)
                .stream()
                .sorted()
                .toList();
        return sumValuesOfHands(sortedHands);
    }

    @Override
    public Object solvePart2() {
        List<PokerHand> sortedHands = handsFromInput(input, true)
                .stream()
                .sorted()
                .toList();
        return sumValuesOfHands(sortedHands);
    }

    private BigInteger sumValuesOfHands(List<PokerHand> sortedHands) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < sortedHands.size(); i++) {
            PokerHand hand = sortedHands.get(i);
            sum = sum.add(BigInteger.valueOf(i + 1).multiply(BigInteger.valueOf(hand.getBid())));
        }
        return sum;
    }

    private List<PokerHand> handsFromInput(List<String> input, boolean jIsJoker) {
        return input.stream().map(line -> new PokerHand(line, jIsJoker)).toList();
    }
}
