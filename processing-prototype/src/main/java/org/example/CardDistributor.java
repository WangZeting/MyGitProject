package org.example;

import java.util.*;

public class CardDistributor {
    private Map<Integer, Card> distributor_alpha;
    private Map<Integer, Card> distributor_beta;
    private Map<Integer, Card> distributor_gamma;

    public Map<Integer, Card> getDistributor_alpha() {
        return distributor_alpha;
    }

    public Map<Integer, Card> getDistributor_beta() {
        return distributor_beta;
    }

    public Map<Integer, Card> getDistributor_gamma() {
        return distributor_gamma;
    }

    public CardDistributor(Set<Card> cardSet) {
        this.distributor_alpha = new HashMap<>();
        this.distributor_beta = new HashMap<>();
        this.distributor_gamma = new HashMap<>();
        int mark = 0;
        int serialNumAlpha = 0;
        int serialNumBeta = 0;
        int serialNumGamma = 0;
        List<Card> cardList = new ArrayList<>();
        for(Card card :cardSet){
            cardList.add(card);
        }
        Collections.shuffle(cardList);
        for (Card card : cardList) {
            mark++;
            switch (mark % 3) {
                case 1:
                    serialNumAlpha++;
                    this.distributor_alpha.put(serialNumAlpha, card);
                    break;
                case 2:
                    serialNumBeta++;
                    this.distributor_beta.put(serialNumBeta, card);
                    break;
                case 0:
                    serialNumGamma++;
                    this.distributor_gamma.put(serialNumGamma, card);
                    break;
            }
        }
    }
}
