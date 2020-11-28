package org.client;

import org.example.Card;
import org.example.CardDeck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    private Map<Integer, Card> cards;
    private int identity;

    public Map<Integer, Card> getCards() {
        return cards;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public Player(Map<Integer, Card> cards, int identity) {
        this.cards = cards;
        this.identity = identity;
    }

    private CardDeck createDeck(int... serialNum) {
        Set<Card> cardSet = new HashSet<>();
        for (int num : serialNum) {
            cardSet.add(this.cards.get(num));
        }
        CardDeck cardDeck = new CardDeck(cardSet);
        return cardDeck;
    }

    private int sendDeck(CardDeck cardDeck) {

        return 1;
    }

    private void deleteCard(int... serialNum) {
        for (int num : serialNum) {
            this.cards.remove(num);
        }
    }

    public void playCard(int... serialNum) {
        CardDeck cardDeck = this.createDeck(serialNum);
        if (this.sendDeck(cardDeck) == 1)
            this.deleteCard();
    }
}
