package org.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectButtonActionListener implements ActionListener {
    public static int markNumber = -1;

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("叫抢")) {
            markNumber = 1;
        }
        if (actionCommand.equals("不抢")) {
            markNumber = 0;
        }
        if (actionCommand.equals("提交")) {
            CardButtonActionListener.selectedNumbers.add(0);
        }
    }
}
