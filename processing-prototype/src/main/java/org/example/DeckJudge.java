package org.example;

import java.util.HashSet;

public class DeckJudge {
    private static CardDeck nowDeck = new CardDeck(new HashSet<Card>());

    public static int judge(CardDeck yourDeck) {
        int judgement = analysing(nowDeck, yourDeck);
        if (judgement == 222) {
            nowDeck = yourDeck;
            return judgement;
        }
        return judgement;
    }

    public static CardDeck getNowDeck() {
        return nowDeck;
    }

    public static int analysing(CardDeck preDeck, CardDeck yourDeck) {
        if (preDeck.getDeck().size() == 0)
            return 222;
        if (preDeck.getTypeSum() == yourDeck.getTypeSum() && preDeck.getCardSum() == yourDeck.getCardSum() && preDeck.getMainSize() == yourDeck.getMainSize()) {
            int type = preDeck.getTypeSum();
            int sum = preDeck.getCardSum();
            int mainSize = preDeck.getMainSize();
            int preMainNumber = preDeck.getMainNumber();
            int yourMainNumber = yourDeck.getMainNumber();
            int preLargestNumber = preDeck.getLargestNumber();
            int yourLargestNumber = yourDeck.getLargestNumber();
            if (type == 1 && yourMainNumber > preMainNumber)
                return 222;
            if (type == 2 && sum < 6 && yourMainNumber > preMainNumber)
                return 222;
            if (type == 3 && mainSize == 4 && yourMainNumber > preMainNumber)
                return 222;
            if (mainSize == 1 && sum > 4 && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 222;
            if (mainSize == 2 && sum > 5 && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 222;
            if (mainSize == 3 && sum > 5 && (sum / 3) == type && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 222;
            if (mainSize == 3 && sum > 7 && (sum / 3) != type && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 222;
        }
        if (yourDeck.getTypeSum() == 1 && yourDeck.getCardSum() == 4)
            return 222;
        if (yourDeck.getLargestNumber() == 15)
            return 222;
        return 223;
    }
}
