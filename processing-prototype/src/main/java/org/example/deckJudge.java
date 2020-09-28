package org.example;

public class deckJudge {
    public deckJudge(CardDeck preDeck, CardDeck yourDeck){
        if(preDeck.getTypeSum()==yourDeck.getTypeSum()&&preDeck.getCardSum()==yourDeck.getCardSum()&&preDeck.getMainNumber()<yourDeck.getMainNumber())
            System.out.println("能打");
    }
}
