package org.example;

import java.util.Map;
import java.util.Set;

public class CardDistributor {
    private Map<Integer, Card> distributor_alpha;
    private Map<Integer, Card> distributor_beta;
    private Map<Integer, Card> distributor_gamma;

    public CardDistributor(Set<Card> cardSet) {
        int mark = 0;
        int serialNumAlpha = 0;
        int serialNumBeta = 0;
        int serialNumGamma = 0;
        for (Card card : cardSet) {
            mark++;
            int remainder = mark % 3;
            switch (remainder) {
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
