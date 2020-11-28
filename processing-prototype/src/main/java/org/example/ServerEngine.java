package org.example;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerEngine {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(10001);
            for (int i = 0; i < 3; i++) {
                System.out.println("  [ServerEngine]  waiting for socket connection no."+i);
                socket = serverSocket.accept();
                new Thread(new ThreadOfPlayer(socket, i)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
