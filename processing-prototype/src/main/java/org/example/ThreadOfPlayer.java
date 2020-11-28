package org.example;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ThreadOfPlayer implements Runnable {

    private Socket socket;
    private int mySequence;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private static int sequenceCursor = 0;//当前线程序号
    private static int gameStatus = 0;//对局状态码
    private static int landlordTemp = -1;//叫地主序号，对应线程序号
    private static int landlord = -1;//地主序号，对应线程序号
    private static CardDistributor cardDistributor = new CardDistributor(CardCreator.createCards());//发牌器
    private static Set<Map<Integer, Card>> cardStack = cardDistributor.getCardSet();//待发的三个牌堆
    private static Map<Integer, Card> lastThreeCards = cardDistributor.getThreeLandlordCards();//最后三张地主牌
    private static CardDeck nowDeck = null;//当前出的牌
    private static int winner = -1;

    public ThreadOfPlayer(Socket socket, int mySequence) {
        this.socket = socket;
        this.mySequence = mySequence;
        try {
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        transmittingCards();
        competing();
        competingAgain();
        getLastThreeCards();
        playCards();
    }

    //发牌
    public void transmittingCards() {
        Iterator iterator = cardStack.iterator();
        Map<Integer, Card> myCards = (HashMap<Integer, Card>) iterator.next();
        try {
            int firstCode = (int) this.objectInputStream.readObject();
            if (firstCode == 111) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 111 ,preparing to transmit 17 cards");
                this.objectOutputStream.writeObject(myCards);
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //叫地主
    public void competing() {
        try {
            int firstCode = (int) this.objectInputStream.readObject();
            if (firstCode == 112) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 112 compete");
                if (landlordTemp == -1) {
                    landlordTemp = this.mySequence;
                    this.objectOutputStream.writeObject(211);
                } else {
                    this.objectOutputStream.writeObject(212);
                }
            } else if (firstCode == 113) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 113 give up competing");
                this.objectOutputStream.writeObject(212);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //抢地主
    public void competingAgain() {
        try {
            int firstCode = (int) this.objectInputStream.readObject();
            if (firstCode == 112) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 112 compete");
                if (landlord == -1) {
                    landlord = this.mySequence;
                    this.objectOutputStream.writeObject(211);
                } else {
                    this.objectOutputStream.writeObject(212);
                }
            } else if (firstCode == 113) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 113 give up competing");
                this.objectOutputStream.writeObject(212);
                Thread.sleep(20000);
            }
            if (landlord == -1)
                landlord = landlordTemp;
            int secondCode = (int) this.objectInputStream.readObject();
            if (secondCode == 114) {
                if (this.mySequence == landlord)
                    this.objectOutputStream.writeObject(211);
                else
                    this.objectOutputStream.writeObject(212);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //地主获取最后三张牌
    public void getLastThreeCards() {
        try {
            int firstCode = (int) this.objectInputStream.readObject();
            if (firstCode == 115) {
                System.out.println("[Thread " + this.mySequence + "] Get Code 115 transmitting last three");
                if (landlord == this.mySequence)
                    this.objectOutputStream.writeObject(lastThreeCards);
                else
                    this.objectOutputStream.writeObject(new HashMap<Integer, Card>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打牌
    public void playCards() {
        while (gameStatus != 1) {
            try {
                int firstCode = (int) this.objectInputStream.readObject();
                if (firstCode == 123) {
                    this.objectOutputStream.writeObject(DeckJudge.getNowDeck());
                }
                int secondCode = (int) this.objectInputStream.readObject();
                if (secondCode == 121) {
                    if (this.mySequence == sequenceCursor) {
                        this.objectOutputStream.writeObject(221);
                        Object object = this.objectInputStream.readObject();
                        if (object instanceof Integer && (int) object == 122) {
                            System.out.println("[Thread " + this.mySequence + "] Get Code 122");
                            sequenceCursor = (sequenceCursor + 1) % 3;
                            this.objectOutputStream.writeObject(300);
                        } else {
                            System.out.println("[Thread " + this.mySequence + "] Get deck");
                            CardDeck deckOfPlayer = (CardDeck) object;
                            int judgement = DeckJudge.judge(deckOfPlayer);
                            if (judgement == 222)
                                sequenceCursor = (sequenceCursor + 1) % 3;
                            this.objectOutputStream.writeObject(judgement);
                        }
                    } else {
                        this.objectOutputStream.writeObject(224);
                    }
                }
                int thirdCode = (int) objectInputStream.readObject();
                if (thirdCode == 0) {
                    gameStatus = 1;
                    winner = this.mySequence;
                    System.out.println("[Thread " + this.mySequence + "] win!");
                    objectOutputStream.writeObject(231);
                } else
                    objectOutputStream.writeObject(232);

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
