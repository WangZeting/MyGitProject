package org.example;

import java.util.HashSet;
import java.util.Set;

public class CardCreator {
    /*enum Color {
        HEART, DIAMOND, SPADE, CLUB;
    }

    enum Name {
        _A, _2, _3, _4, _5, _6, _7, _8, _9, _10, _J, _Q, _K;
    }*/

    public static Set<Card> createCards() {
        /*for (Color color : Color.values()) {
            for (Name name : Name.values()) {
                Card card = new Card(name.toString(), color.toString(), name.ordinal() + 1);
                cardSet.add(card);
            }
        }*/
        Set<Card> cardSet = new HashSet<>();
        String[] colors = {"♥", "♦", "♣", "♠"};
        String[] numbers = {"A ", "2 ", "3 ", "4 ", "5 ", "6 ", "7 ", "8 ", "9 ", "10", "J ", "Q ", "K "};
        for (int i = 1; i <= 13; i++) {
            for (String color : colors) {
                Card card = new Card(numbers[i-1], color, i);
                cardSet.add(card);
            }
        }
        cardSet.add(new Card("Jk", "♥", 15));
        cardSet.add(new Card("jk", "♠", 14));
        return cardSet;
    }
}
