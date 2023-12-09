package com.markus.day2;

import com.markus.Day;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameChecker extends Day {

    public GameChecker(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        return reduceLines(input);
    }

    @Override
    public Object solvePart2() {
        return getPowers(input);
    }
    private static final Map<CubeGame.SetColor, Long> constraints = Map.of(
            CubeGame.SetColor.RED, 12L,
            CubeGame.SetColor.GREEN, 13L,
            CubeGame.SetColor.BLUE, 14L
    );

    public Long reduceLines(List<String> lines) {

        return lines.stream()
                .parallel()
                .map(CubeGame::new)
                .filter(game -> game.satisFiesConstraint(constraints))
                .map(CubeGame::getId)
                .reduce(Long::sum).orElse(0L);
    }

    public BigInteger getPowers(List<String> lines) {

        return lines.stream()
                .parallel()
                .map(CubeGame::new)
                .map(CubeGame::getPower)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }
}
