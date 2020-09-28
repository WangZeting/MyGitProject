package org.example;

public class DeckJudge {
    private CardDeck preDeck;
    private CardDeck yourDeck;
    private int judgement;

    public CardDeck getPreDeck() {
        return preDeck;
    }

    public CardDeck getYourDeck() {
        return yourDeck;
    }

    public int getJudgement() {
        return judgement;
    }

    public DeckJudge(CardDeck preDeck, CardDeck yourDeck) {
        this.preDeck = preDeck;
        this.yourDeck = yourDeck;
        this.judgement = this.judge(this.preDeck, this.yourDeck);
    }

    public int judge(CardDeck preDeck, CardDeck yourDeck) {
        if (preDeck.getTypeSum() == yourDeck.getTypeSum() && preDeck.getCardSum() == yourDeck.getCardSum() && preDeck.getMainSize() == yourDeck.getMainSize()) {
            int type = preDeck.getTypeSum();
            int sum = preDeck.getCardSum();
            int mainSize = preDeck.getMainSize();
            int preMainNumber = preDeck.getMainNumber();
            int yourMainNumber = yourDeck.getMainNumber();
            int preLargestNumber = preDeck.getLargestNumber();
            int yourLargestNumber = yourDeck.getLargestNumber();
            if (type == 1 && yourMainNumber > preMainNumber)
                return 1;
            if (type == 2 && sum < 6 && yourMainNumber > preMainNumber)
                return 1;
            if (type == 3 && mainSize == 4 && yourMainNumber > preMainNumber)
                return 1;
            if (mainSize == 1 && sum > 4 && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 1;
            if (mainSize == 2 && sum > 5 && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 1;
            if (mainSize == 3 && sum > 5 && (sum / 3) == type && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 1;
            if (mainSize == 3 && sum > 7 && (sum / 3) != type && preLargestNumber < 13 && yourLargestNumber < 13 && yourLargestNumber > preLargestNumber)
                return 1;
        }
        if (yourDeck.getTypeSum() == 1 && yourDeck.getCardSum() == 4)
            return 1;
        if (yourDeck.getLargestNumber() == 15)
            return 1;
        return 0;
    }
}
