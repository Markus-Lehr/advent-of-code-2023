package com.markus.day6;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Race {
    private Long duration;
    private Long distance;

    public Long numbersOfWaysToBeatDistance() {
        Long result = 0L;

        for (int speed = 1; speed <= duration; speed++) {
            long remainingTime = duration - speed;
            long traveledDistance = remainingTime * speed;
            if (traveledDistance > distance) {
                result++;
            }
        }

        return result;
    }
}
