package org.example;

import java.util.*;

public class CardDistributor {
    private Set<Map<Integer, Card>> cardSet;
    private Map<Integer, Card> threeLandlordCards;

    public Set<Map<Integer, Card>> getCardSet() {
        return cardSet;
    }

    public Map<Integer, Card> getThreeLandlordCards() {
        return threeLandlordCards;
    }

    public CardDistributor(Set<Card> inputCardSet) {
        Map<Integer, Card> cardsAlpha = new HashMap<>();
        Map<Integer, Card> cardsBeta = new HashMap<>();
        Map<Integer, Card> cardsGamma = new HashMap<>();
        cardSet = new HashSet<>();
        this.threeLandlordCards = new HashMap<>();
        int mark = 0;
        int serialNumAlpha = 0;
        int serialNumBeta = 0;
        int serialNumGamma = 0;
        List<Card> cardList = new ArrayList<>();
        for (Card card : inputCardSet) {
            cardList.add(card);
        }
        Collections.shuffle(cardList);
        for (Card card : cardList) {
            if (mark >= 51) {
                mark++;
                serialNumAlpha++;
                this.threeLandlordCards.put(serialNumAlpha, card);
                continue;
            }
            mark++;
            switch (mark % 3) {
                case 1:
                    serialNumAlpha++;
                    cardsAlpha.put(serialNumAlpha, card);
                    break;
                case 2:
                    serialNumBeta++;
                    cardsBeta.put(serialNumBeta, card);
                    break;
                case 0:
                    serialNumGamma++;
                    cardsGamma.put(serialNumGamma, card);
                    break;
            }
        }
        this.cardSet.add(cardsAlpha);
        this.cardSet.add(cardsBeta);
        this.cardSet.add(cardsGamma);
    }
}
