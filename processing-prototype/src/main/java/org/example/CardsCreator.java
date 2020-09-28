package org.example;

import java.util.HashSet;
import java.util.Set;

public class CardsCreator {
    enum Color{
        HEART,DIAMOND,SPADE,CLUB;
    }
    enum Name{
        CARD_A,CARD_2,CARD_3,CARD_4,CARD_5,CARD_6,CARD_7,CARD_8,CARD_9,CARD_10,CARD_J,CARD_Q,CARD_K;
    }
    public Set<Card> createCards(){
        Set<Card> cardSet = new HashSet<>();
        for (Color color : Color.values()){
            for(Name name : Name.values()){
                Card card = new Card(name.toString(),color.toString(),name.ordinal()+1);
                cardSet.add(card);
            }
        }
        return cardSet;
    }
}
