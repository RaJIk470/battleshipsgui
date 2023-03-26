package org.example.net;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Data
public class ChatServer extends ChatUser implements Runnable {
    private ServerSocket serverSocket;
    private Game game;

    private Listener listener;
    private PrintWriter out;
    private Scanner in;

    public ChatServer(Game game, Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.game = game;
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    @Override
    @SneakyThrows
    public void run() {
        Socket socket = serverSocket.accept();
        try {

            out = new PrintWriter(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());
            listener = new Listener(in);
            Thread thread = new Thread(listener);
            thread.start();
            while (socket.isConnected()) {
                while (!messages.isEmpty()) {
                    String msg = game.getUsername() + ": " + messages.pollFirst() + "\n";
                    out.print(msg);
                    out.flush();
                    game.getMainForm().getBp().getChatArea().append(msg);
                }
                while (!listener.getMessages().isEmpty()) {
                    String msg = listener.getMessages().pollFirst() + "\n";
                    game.getMainForm().getBp().getChatArea().append(msg);
                }
                Thread.sleep(500);
            }
        } finally {
            System.out.println("Stopping");
            serverSocket.close();
            socket.close();
        }
    }

}
