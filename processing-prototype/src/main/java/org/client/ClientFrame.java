package org.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientFrame {
    private JFrame jFrame;
    private JPanel firstLinePanel;
    private JPanel secondLinePanel;
    private JPanel thirdLinePanel;
    private JScrollPane rightSidePanel;
    private JTextArea jTextArea;

    public JFrame getjFrame() {
        return jFrame;
    }

    public JPanel getFirstLinePanel() {
        return firstLinePanel;
    }

    public JPanel getSecondLinePanel() {
        return secondLinePanel;
    }

    public JTextArea getjTextArea() {
        return jTextArea;
    }

    public ClientFrame() {
        this.jFrame = new JFrame("ClientUI");
        this.jFrame.setSize(450, 360);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.jFrame.setLayout(null);
        initializePanels();
        addPanels();
        initializeAndAddComponents();
        this.jFrame.setVisible(true);
    }

    private void initializePanels() {
        this.firstLinePanel = new JPanel(new FlowLayout());
        this.firstLinePanel.setBounds(10, 10, 300, 50);
        this.secondLinePanel = new JPanel(new FlowLayout());
        this.secondLinePanel.setBounds(10, 70, 300, 200);
        this.thirdLinePanel = new JPanel(new FlowLayout());
        this.thirdLinePanel.setBounds(10, 280, 300, 50);
        this.jTextArea = new JTextArea();
        this.rightSidePanel = new JScrollPane(jTextArea);
        this.rightSidePanel.setBounds(320, 10, 100, 300);
    }

    private void addPanels() {
        this.jFrame.add(firstLinePanel);
        this.jFrame.add(secondLinePanel);
        this.jFrame.add(thirdLinePanel);
        this.jFrame.add(rightSidePanel);
    }

    private void initializeAndAddComponents() {
        ActionListener selectButtonActionListener = new SelectButtonActionListener();
        JButton robButton = new JButton("叫抢");
        robButton.addActionListener(selectButtonActionListener);
        JButton giveUpRobButton = new JButton("不抢");
        giveUpRobButton.addActionListener(selectButtonActionListener);
        JButton submitButton = new JButton("提交");
        submitButton.addActionListener(selectButtonActionListener);

        this.thirdLinePanel.add(robButton);
        this.thirdLinePanel.add(giveUpRobButton);
        this.thirdLinePanel.add(submitButton);

    }

    public void addToTextArea(String content) {
        this.jTextArea.append(" " + content + "\r\n");
    }
}
