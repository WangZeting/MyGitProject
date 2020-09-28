package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardDeck {
    private Set<Card> deck;
    private int typeSum;
    private int cardSum;
    private int mainNumber;

    public int getTypeSum() {
        return typeSum;
    }

    public int getCardSum() {
        return cardSum;
    }

    public int getMainNumber() {
        return mainNumber;
    }

    public void analysing() {
        typeSum = 0;
        cardSum = deck.size();
        mainNumber = 0;
        Map<Integer, Integer> cardMap = new HashMap<>();
        for (Card card : deck) {
            int number = card.getCardRank();
            if (cardMap.containsKey(number))
                cardMap.put(number, cardMap.get(number) + 1);
            else {
                cardMap.put(number, 1);
                typeSum++;
            }
        }
        for (Map.Entry<Integer, Integer> entry : cardMap.entrySet()) {
            int mainSize = 0;
            if (mainSize < entry.getValue()) {
                mainSize = entry.getValue();
                mainNumber = entry.getKey();
            }
        }
    }
}
