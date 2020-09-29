package org.example;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerEngine {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            serverSocket = new ServerSocket(10000);
            socket = serverSocket.accept();
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());

            if ((int) objectInputStream.readObject() == 101) {
                System.out.println("receive 101");
                CardDistributor cardDistributor = new CardDistributor(CardCreator.createCards());
                objectOutputStream.writeObject(cardDistributor.getDistributor_alpha());
                System.out.println("cards sent");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
                serverSocket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
