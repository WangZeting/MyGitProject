package org.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class CardButtonActionListener implements ActionListener {
    public static Set<Integer> selectedNumbers = new HashSet<>();
    private int cardNumber;

    public CardButtonActionListener(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedNumbers.add(this.cardNumber);
    }
}
