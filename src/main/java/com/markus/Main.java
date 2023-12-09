package com.markus;

import com.markus.day1.StartEndReducer;
import com.markus.day2.GameChecker;
import com.markus.day3.EngineReducer;
import com.markus.day4.ScratchCardEvaluator;
import com.markus.day5.SeedMapper;
import com.markus.day6.BoatRacer;
import com.markus.day7.PokerSolver;
import com.markus.day8.LeftRightCamel;
import com.markus.day9.SequenceExtrapolator;

public class Main {
    public static void main(String[] args) {
        DayRunner dayRunner = new DayRunner(
                StartEndReducer::new,
                GameChecker::new,
                EngineReducer::new,
                ScratchCardEvaluator::new,
                SeedMapper::new,
                BoatRacer::new,
                PokerSolver::new,
                LeftRightCamel::new,
                SequenceExtrapolator::new
        );
        dayRunner.runDay(5);
    }
}
