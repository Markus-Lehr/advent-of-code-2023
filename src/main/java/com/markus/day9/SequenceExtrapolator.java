package com.markus.day9;

import com.markus.Day;

import java.math.BigInteger;
import java.util.List;

public class SequenceExtrapolator extends Day {
    public SequenceExtrapolator(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        return input.stream()
                .parallel()
                .map(NumberSequence::new)
                .map(NumberSequence::extrapolate)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    @Override
    public Object solvePart2() {
        return input.stream()
                .parallel()
                .map(NumberSequence::new)
                .map(NumberSequence::leftrapolate)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }
}
