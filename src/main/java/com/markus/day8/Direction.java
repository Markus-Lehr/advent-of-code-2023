package com.markus.day8;

public enum Direction {
    LEFT, RIGHT;

    public static Direction fromLetter(char letter) {
        return switch (letter) {
            case 'L' -> LEFT;
            case 'R' -> RIGHT;
            default -> throw new IllegalArgumentException("Cannot create direction from letter " + letter);
        };
    }
}
