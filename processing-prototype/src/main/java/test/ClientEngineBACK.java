package test;

import org.client.CardButtonActionListener;
import org.client.ClientFrame;
import org.client.Player;
import org.client.SelectButtonActionListener;
import org.example.Card;
import org.example.CardDeck;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientEngineBACK {
    public static void main(String[] args) {
        /*JFrame jFrame = new JFrame("client");
        jFrame.setSize(360, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel basePanel = new JPanel(new GridLayout(4, 1));
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel middlePanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JPanel consolePanel = new JPanel(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane(consolePanel);

        JButton robButton = new JButton("robIdentity");
        JButton giveUpRobButton = new JButton("giveUpRobbing");
        JButton submitButton = new JButton("submit");
        JTextArea textArea = new JTextArea();

        ActionListener selectButtonActionListener = new SelectButtonActionListener();
        robButton.addActionListener(selectButtonActionListener);
        giveUpRobButton.addActionListener(selectButtonActionListener);
        submitButton.addActionListener(selectButtonActionListener);

        bottomPanel.add(robButton);
        bottomPanel.add(giveUpRobButton);
        bottomPanel.add(submitButton);
        consolePanel.add(textArea);
        basePanel.add(topPanel);
        basePanel.add(middlePanel);
        basePanel.add(bottomPanel);
        basePanel.add(scrollPane);
        jFrame.setContentPane(basePanel);

        for (int i=0;i<20;i++){
            textArea.append("text12345678\r\n");
        }
        jFrame.setVisible(true);*/
        ClientFrame frame = new ClientFrame();

        Socket socket = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        Player player = null;
        try {
            //初始化socket和objectStream
            socket = new Socket("127.0.0.1", 10001);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            //发送111 request cards
            objectOutputStream.writeObject(111);
            frame.addToTextArea("send 111(request cards)");
            Map<Integer, Card> myCards = (Map<Integer, Card>) objectInputStream.readObject();
            player = new Player(myCards, 0);
            printCardToFrame(player.getCards(), frame.getSecondLinePanel());
            frame.addToTextArea("get cards and print");
            //叫抢
            frame.addToTextArea("click to choose");
            int input = waitForClicking();
            if (input == 1) {
                objectOutputStream.writeObject(112);
                frame.addToTextArea("send 112(choose to ask for landlord)");
                int competeReturnCode = (int) objectInputStream.readObject();

                if (competeReturnCode == 211) {
                    frame.addToTextArea("get 211(ask for landlord successfully)");
                    objectOutputStream.writeObject(113);
                }
                if (competeReturnCode == 212) {
                    frame.addToTextArea("get 212(ask for landlord failed)");
                    frame.addToTextArea("click to choose again");
                    input = waitForClicking();
                    if (input == 1) {
                        objectOutputStream.writeObject(112);
                        frame.addToTextArea("send 112(choose to rob)");
                    }
                    if (input == 0) {
                        objectOutputStream.writeObject(113);
                        frame.addToTextArea("send 113(give up robbing)");
                    }
                }
            } else if (input == 0) {
                objectOutputStream.writeObject(113);
                frame.addToTextArea("send 113(give up asking for landlord)");
                objectInputStream.readObject();
                frame.addToTextArea("click to choose again");
                input = waitForClicking();
                if (input == 1) {
                    objectOutputStream.writeObject(112);
                    frame.addToTextArea("send 112(choose to rob)");
                } else if (input == 0)
                    objectOutputStream.writeObject(113);
                frame.addToTextArea("send 113(give up robbing)");
            }
            objectInputStream.readObject();
            //request争抢结果
            frame.addToTextArea("send 114(request competing result)");
            objectOutputStream.writeObject(114);
            int finalCompeteReturnCode = (int) objectInputStream.readObject();
            if (finalCompeteReturnCode == 211) {
                frame.addToTextArea("you are the landlord");
                player.setIdentity(1);
            } else if (finalCompeteReturnCode == 212) {
                frame.addToTextArea("you are a peasant");
            }
            //request最后三张牌
            frame.addToTextArea("send 115(request the final three cards)");
            objectOutputStream.writeObject(115);
            Map<Integer, Card> lastThreeCards = (Map<Integer, Card>) objectInputStream.readObject();
            for (Map.Entry<Integer, Card> entry : lastThreeCards.entrySet()) {
                player.getCards().put(entry.getKey(), entry.getValue());
            }
            printCardToFrame(player.getCards(), frame.getSecondLinePanel());
            frame.addToTextArea("refresh the cards");
            //循环打牌
            while (true) {
                //申请刷新已出cards
                objectOutputStream.writeObject(123);
                frame.addToTextArea("send 123(request the newest deck)");
                CardDeck nowDeck = (CardDeck) objectInputStream.readObject();
                //请求许可
                objectOutputStream.writeObject(121);
                frame.addToTextArea("send 121(request permission to play)");
                int firstCode = (int) objectInputStream.readObject();
                //服务器返回许可
                if (firstCode == 221) {
                    frame.addToTextArea("get 221(allowed to play)");
                    refreshTopPanel(nowDeck, frame.getFirstLinePanel());
                    frame.addToTextArea("refresh the newest deck");
                    printCardToFrame(player.getCards(), frame.getSecondLinePanel());
                    frame.addToTextArea("refresh your cards");
                    frame.addToTextArea("click the cards and submit button");
                    frame.addToTextArea("click submit only to pass");
                    Set<Integer> serialNum = waitForClickingCards();
                    if (serialNum.size() == 0) {
                        objectOutputStream.writeObject(122);
                        frame.addToTextArea("pass this round");
                        objectInputStream.readObject();
                        objectOutputStream.writeObject(player.getCards().size());
                        objectInputStream.readObject();
                        continue;
                    }
                    CardDeck deckOfSerialNum = getCardDeck(player.getCards(), serialNum);
                    objectOutputStream.writeObject(deckOfSerialNum);
                    frame.addToTextArea("send deck to judge");
                    int secondCode = (int) objectInputStream.readObject();
                    if (secondCode == 222) {
                        frame.addToTextArea("play successfully");
                        deleteCard(player.getCards(), serialNum);
                    }
                    if (secondCode == 223) {
                        frame.addToTextArea("can not play ,try again");
                    }
                }
                //不论是否许可，每次循环均发送剩余数量,并等候两秒继续循环
                objectOutputStream.writeObject(player.getCards().size());
                int statusCode=(int)objectInputStream.readObject();
                if (statusCode==231){
                    frame.addToTextArea("you win!");
                }
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
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
