package com.markus.day8;

import com.markus.Day;

import java.util.List;

public class LeftRightCamel extends Day {
    public LeftRightCamel(List<String> input) {
        super(input);
    }

    @Override
    public Object solvePart1() {
        CamelMap camelMap = fromInput(input);
        return camelMap.traverse("AAA", "ZZZ");
    }

    @Override
    public Object solvePart2() {
        CamelMap camelMap = fromInput(input);
        return camelMap.traverse();
    }

    private CamelMap fromInput(List<String> lines) {
        return new CamelMap(
                lines.get(0),
                lines.subList(2, lines.size())
        );
    }
}
