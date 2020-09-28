package org.example;

public class Card {
    private String cardName;
    private String cardColor;
    private int cardRank;

    public String getCardName() {
        return cardName;
    }

    public String getCardColor() {
        return cardColor;
    }

    public int getCardRank() {
        return cardRank;
    }

    public Card(String cardName, String cardColor, int cardRank) {
        this.cardName = cardName;
        this.cardColor = cardColor;
        this.cardRank = cardRank;
    }
}
