package org.client;

import org.example.Card;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

public class ClientEngine {
    public static void main(String[] args) {
        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            socket = new Socket("127.0.0.1", 10000);
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(101);
            System.out.println("send 101");
            Map<Integer, Card> yourCards = (Map<Integer, Card>) objectInputStream.readObject();
            for (Map.Entry<Integer,Card> entry :yourCards.entrySet()){
                System.out.println(entry.getValue().getCardName()+entry.getValue().getCardColor());
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
}
