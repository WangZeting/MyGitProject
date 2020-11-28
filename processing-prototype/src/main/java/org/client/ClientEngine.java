package org.client;

import org.example.Card;
import org.example.CardDeck;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ClientEngine {
    private ClientFrame frame;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Player player;

    public static void main(String[] args) throws Exception {
        JFrame inputFrame = new JFrame();
        String host = JOptionPane.showInputDialog(inputFrame, "host", "127.0.0.1");
        ClientEngine clientEngine = new ClientEngine(host);
        clientEngine.run();
    }

    public ClientEngine(String host) throws Exception {
        this.frame = new ClientFrame();
        this.socket = new Socket(host, 10001);
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    }

    public void run() throws Exception {
        requestCards();
        askForTheIdentityAndCompete();
        askForCompetingResult();
        requestLastThreeCards();
        repeatPlaying();
        closeConnect();
    }

    private void requestCards() throws Exception {
        this.objectOutputStream.writeObject(111);
        this.frame.addToTextArea("send 111(request cards)");
        Map<Integer, Card> myCards = (Map<Integer, Card>) this.objectInputStream.readObject();
        this.player = new Player(myCards, 0);
        printCardToFrame(this.player.getCards(), this.frame.getSecondLinePanel());
        this.frame.addToTextArea("get cards and print");
    }

    private void askForTheIdentityAndCompete() throws Exception {
        this.frame.addToTextArea("click to choose");
        int input = waitForClicking();
        if (input == 1) {
            this.objectOutputStream.writeObject(112);
            this.frame.addToTextArea("send 112(choose to ask for landlord)");
            int competeReturnCode = (int) this.objectInputStream.readObject();

            if (competeReturnCode == 211) {
                this.frame.addToTextArea("get 211(ask for landlord successfully)");
                this.objectOutputStream.writeObject(113);
            }
            if (competeReturnCode == 212) {
                this.frame.addToTextArea("get 212(ask for landlord failed)");
                this.frame.addToTextArea("click to choose again");
                input = waitForClicking();
                if (input == 1) {
                    this.objectOutputStream.writeObject(112);
                    this.frame.addToTextArea("send 112(choose to rob)");
                }
                if (input == 0) {
                    this.objectOutputStream.writeObject(113);
                    this.frame.addToTextArea("send 113(give up robbing)");
                }
            }
        } else if (input == 0) {
            this.objectOutputStream.writeObject(113);
            this.frame.addToTextArea("send 113(give up asking for landlord)");
            this.objectInputStream.readObject();
            this.frame.addToTextArea("click to choose again");
            input = waitForClicking();
            if (input == 1) {
                this.objectOutputStream.writeObject(112);
                this.frame.addToTextArea("send 112(choose to rob)");
            } else if (input == 0)
                this.objectOutputStream.writeObject(113);
            this.frame.addToTextArea("send 113(give up robbing)");
        }
        this.objectInputStream.readObject();
    }

    private void askForCompetingResult() throws Exception {
        this.frame.addToTextArea("send 114(request competing result)");
        this.objectOutputStream.writeObject(114);
        int finalCompeteReturnCode = (int) this.objectInputStream.readObject();
        if (finalCompeteReturnCode == 211) {
            this.frame.addToTextArea("you are the landlord");
            this.player.setIdentity(1);
        } else if (finalCompeteReturnCode == 212) {
            this.frame.addToTextArea("you are a peasant");
        }
    }

    private void requestLastThreeCards() throws Exception {
        this.frame.addToTextArea("send 115(request the final three cards)");
        this.objectOutputStream.writeObject(115);
        Map<Integer, Card> lastThreeCards = (Map<Integer, Card>) this.objectInputStream.readObject();
        for (Map.Entry<Integer, Card> entry : lastThreeCards.entrySet()) {
            this.player.getCards().put(entry.getKey(), entry.getValue());
        }
        printCardToFrame(this.player.getCards(), this.frame.getSecondLinePanel());
        this.frame.addToTextArea("refresh the cards");
    }

    private void repeatPlaying() throws Exception {
        while (true) {
            //申请刷新已出cards
            this.objectOutputStream.writeObject(123);
            this.frame.addToTextArea("send 123(request the newest deck)");
            CardDeck nowDeck = (CardDeck) this.objectInputStream.readObject();
            //请求许可
            this.objectOutputStream.writeObject(121);
            this.frame.addToTextArea("send 121(request permission to play)");
            int firstCode = (int) this.objectInputStream.readObject();
            //服务器返回许可
            if (firstCode == 221) {
                this.frame.addToTextArea("get 221(allowed to play)");
                refreshTopPanel(nowDeck, this.frame.getFirstLinePanel());
                this.frame.addToTextArea("refresh the newest deck");
                printCardToFrame(this.player.getCards(), this.frame.getSecondLinePanel());
                this.frame.addToTextArea("refresh your cards");
                this.frame.addToTextArea("click the cards and submit button");
                this.frame.addToTextArea("click submit only to pass");
                Set<Integer> serialNum = waitForClickingCards();
                if (serialNum.size() == 0) {
                    this.objectOutputStream.writeObject(122);
                    this.frame.addToTextArea("pass this round");
                    this.objectInputStream.readObject();
                    this.objectOutputStream.writeObject(this.player.getCards().size());
                    this.objectInputStream.readObject();
                    continue;
                }
                CardDeck deckOfSerialNum = getCardDeck(this.player.getCards(), serialNum);
                this.objectOutputStream.writeObject(deckOfSerialNum);
                this.frame.addToTextArea("send deck to judge");
                int secondCode = (int) this.objectInputStream.readObject();
                if (secondCode == 222) {
                    this.frame.addToTextArea("play successfully");
                    deleteCard(this.player.getCards(), serialNum);
                }
                if (secondCode == 223) {
                    this.frame.addToTextArea("can not play ,try again");
                }
            }
            //不论是否许可，每次循环均发送剩余数量,并等候两秒继续循环
            this.objectOutputStream.writeObject(this.player.getCards().size());
            int statusCode = (int) this.objectInputStream.readObject();
            if (statusCode == 231) {
                this.frame.addToTextArea("you win!");
            }
            Thread.sleep(2000);
        }
    }

    private void closeConnect() throws Exception {
        this.objectInputStream.close();
        this.objectOutputStream.close();
        this.socket.close();
    }

    //向指定panel中打印刷新cards
    public static void printCardToFrame(Map<Integer, Card> yourCards, JPanel panel) {
        panel.removeAll();
        for (int i = 1; i <= 15; i++) {
            for (Map.Entry<Integer, Card> entry : yourCards.entrySet()) {
                if (entry.getValue().getCardRank() == i) {
                    JButton jButton = new JButton("<html>" + entry.getValue().getCardName() + "<br>" + entry.getValue().getCardColor() + "</html>");
                    jButton.addActionListener(new CardButtonActionListener(entry.getKey()));
                    panel.add(jButton);
                }
            }
        }
        panel.validate();
    }

    //刷新当前打出的cards
    public static void refreshTopPanel(CardDeck deck, JPanel panel) {
        panel.removeAll();
        for (int i = 1; i <= 15; i++) {
            for (Card card : deck.getDeck()) {
                if (card.getCardRank() == i) {
                    JButton jButton = new JButton("<html>" + card.getCardName() + "<br>" + card.getCardColor() + "</html>");
                    jButton.setEnabled(false);
                    panel.add(jButton);
                }
            }
        }
        panel.validate();
    }

    //按钮等待点击
    public static int waitForClicking() {
        SelectButtonActionListener.markNumber = -1;
        while (SelectButtonActionListener.markNumber == -1) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SelectButtonActionListener.markNumber;
    }

    //cards等待点击
    public static Set<Integer> waitForClickingCards() {
        CardButtonActionListener.selectedNumbers.clear();
        while (!CardButtonActionListener.selectedNumbers.contains(0)) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CardButtonActionListener.selectedNumbers.remove(0);
        return CardButtonActionListener.selectedNumbers;
    }

    //根据牌序号构建牌组deck
    public static CardDeck getCardDeck(Map<Integer, Card> myCard, Set<Integer> serialNum) {
        Set<Card> cardSet = new HashSet<>();
        for (int i : serialNum) {
            cardSet.add(myCard.get(i));
        }
        CardDeck cardDeck = new CardDeck(cardSet);
        return cardDeck;
    }

    //根据序号删除手牌中已打出的牌
    public static void deleteCard(Map<Integer, Card> myCard, Set<Integer> serialNum) {
        for (int i : serialNum)
            myCard.remove(i);
    }
}
