package org.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardDeck implements Serializable {
    private Set<Card> deck;
    private int typeSum;
    private int cardSum;
    private int mainNumber;
    private int mainSize;
    private int largestNumber;

    public int getTypeSum() {
        return typeSum;
    }

    public int getCardSum() {
        return cardSum;
    }

    public int getMainNumber() {
        return mainNumber;
    }

    public int getMainSize() {
        return mainSize;
    }

    public int getLargestNumber() {
        return largestNumber;
    }

    public Set<Card> getDeck() {
        return deck;
    }

    public CardDeck(Set<Card> cardSet) {
        this.deck = cardSet;
        this.analysing();
    }

    public void analysing() {
        this.typeSum = 0;
        this.cardSum = this.deck.size();
        this.mainNumber = 0;
        this.mainSize = 0;
        this.largestNumber = 0;
        Map<Integer, Integer> cardMap = new HashMap<>();
        for (Card card : this.deck) {
            int number = card.getCardRank();
            if (cardMap.containsKey(number))
                cardMap.put(number, cardMap.get(number) + 1);
            else {
                cardMap.put(number, 1);
                this.typeSum++;
            }
        }
        for (Map.Entry<Integer, Integer> entry : cardMap.entrySet()) {
            if (this.mainSize < entry.getValue()) {
                this.mainSize = entry.getValue();
                this.mainNumber = entry.getKey();
            }
            if (this.largestNumber < entry.getKey())
                this.largestNumber = entry.getKey();
        }
        if (this.largestNumber == -1)
            return;
        if (this.largestNumber < 14) {
            this.mainNumber -= 2;
            if (this.mainNumber < 1)
                this.mainNumber += 13;
            this.largestNumber -= 2;
            if (this.largestNumber < 1)
                this.largestNumber += 13;
        }
    }
}
