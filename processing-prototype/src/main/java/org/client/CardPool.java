package org.client;

import org.example.Card;
import org.example.CardDeck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardPool {
    private Map<Integer, Card> pool;

    public CardPool(Map<Integer, Card> pool) {
        this.pool = pool;
    }
    private CardDeck createDeck(int... serialNum){
        Set<Card> cardSet = new HashSet<>();
        for (int num : serialNum){
            cardSet.add(this.pool.get(num));
        }
        CardDeck cardDeck = new CardDeck(cardSet);
        return cardDeck;
    }
    private int sendDeck(CardDeck cardDeck){

        return 1;
    }
    private void deleteCard(int... serialNum){
        for(int num : serialNum){
            this.pool.remove(num);
        }
    }
    public void playCard(int... serialNum){
        CardDeck cardDeck = this.createDeck(serialNum);
        if (this.sendDeck(cardDeck)==1)
            this.deleteCard();
    }
}
