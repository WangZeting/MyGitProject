package org.example;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Set<Card> preSet = new HashSet<>();
        preSet.add(new Card("CARD","HEART",3));
        preSet.add(new Card("CARD","HEART",4));
        preSet.add(new Card("CARD","HEART",5));
        preSet.add(new Card("CARD","HEART",6));
        preSet.add(new Card("CARD","HEART",7));
        Set<Card> yourSet = new HashSet<>();
        yourSet.add(new Card("CARD","HEART",4));
        yourSet.add(new Card("CARD","HEART",5));
        yourSet.add(new Card("CARD","HEART",6));
        yourSet.add(new Card("CARD","HEART",7));
        yourSet.add(new Card("CARD","HEART",8));
        CardDeck preDeck = new CardDeck(preSet);
        CardDeck yourDeck = new CardDeck(yourSet);
        int result = new DeckJudge(preDeck,yourDeck).getJudgement();
        System.out.println(result);
    }
}
