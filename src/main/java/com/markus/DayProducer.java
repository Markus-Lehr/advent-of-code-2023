package com.markus;

import java.util.List;

@FunctionalInterface
public interface DayProducer {
    Day produce(List<String> lines);
}
