package com.markus.day7;

import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PokerHand implements Comparable<PokerHand> {
    private static final Map<Character, Long> cardValues = new HashMap<>();
    private static final Map<Character, Long> cardValuesWithJoker = new HashMap<>();
    private static final Long[] highCardMultipliers = new Long[]{10_000_000_000L, 100_000_000L, 1_000_000L, 10_000L, 100L, 1L};
    private static final Long setMultiplier = 1_000_000_000_000L;

    static {
        cardValues.put('2', 2L);
        cardValues.put('3', 3L);
        cardValues.put('4', 4L);
        cardValues.put('5', 5L);
        cardValues.put('6', 6L);
        cardValues.put('7', 7L);
        cardValues.put('8', 8L);
        cardValues.put('9', 9L);
        cardValues.put('T', 10L);
        cardValues.put('J', 11L);
        cardValues.put('Q', 12L);
        cardValues.put('K', 13L);
        cardValues.put('A', 14L);

        cardValuesWithJoker.putAll(cardValues);
        cardValuesWithJoker.put('J', 1L);
    }

    private List<Character> hand;
    private Map<Character, Long> cardCount;
    private Map<Character, Long> cardCountWithJokers;
    private Long bid;
    private final boolean jIsJoker;

    public PokerHand(String line, boolean jIsJoker) {
        this.jIsJoker = jIsJoker;
        String[] parts = line.split(" ");
        hand = new ArrayList<>();
        for (char card : parts[0].toCharArray()) {
            hand.add(card);
        }
        cardCount = hand.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        cardCountWithJokers = new HashMap<>();
        cardCountWithJokers.putAll(cardCount);
        Long jokerCount = cardCountWithJokers.remove('J');
        if (jokerCount != null && jokerCount > 0 && jokerCount < 5) {
            Map.Entry<Character, Long> highestCardInHand = getHighestEntryInMap(cardCountWithJokers);
            cardCountWithJokers.put(highestCardInHand.getKey(), highestCardInHand.getValue() + jokerCount);
        } else if (jokerCount != null && jokerCount == 5) {
            cardCountWithJokers.put('A', 5L);
        }
        bid = Long.parseLong(parts[1]);
    }

    public Long getHighCardValue() {
        Map<Character, Long> valueLookup = jIsJoker ? cardValuesWithJoker : cardValues;

        long result = 0L;
        for (int i = 0; i < 5; i++) {
            Character card = hand.get(i);
            result += valueLookup.get(card) * highCardMultipliers[i];
        }
        return result;
    }

    public Long getValue() {
        Map<Character, Long> usedCardCount = jIsJoker ? cardCountWithJokers : cardCount;

        Long highCardValue = getHighCardValue();
        long setValue = 0L;
        long maxCount = getHighestEntryInMap(usedCardCount).getValue();
        if (maxCount == 5) {
            // five of a kind
            setValue = 6L;
        } else if (maxCount == 4) {
            // four of a kind
            setValue = 5L;
        } else if (maxCount == 3 && usedCardCount.size() == 2) {
            // full house
            setValue = 4L;
        } else if (maxCount == 3) {
            // three of a kind
            setValue = 3L;
        } else if (maxCount == 2 && usedCardCount.size() == 3) {
            // two pair
            setValue = 2L;
        } else if (maxCount == 2) {
            // one pair
            setValue = 1L;
        }
        return setValue * setMultiplier + highCardValue;
    }

    @Override
    public int compareTo(PokerHand o) {
        return getValue().compareTo(o.getValue());
    }

    private <K, V extends Comparable<V>> Map.Entry<K, V>  getHighestEntryInMap(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
    }
}
