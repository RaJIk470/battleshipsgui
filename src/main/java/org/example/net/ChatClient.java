package org.example.net;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

@Data
public class ChatClient extends ChatUser implements Runnable {
    private Socket socket;
    private Game game;

    private Listener listener;
    private PrintWriter out;
    private Scanner in;

    public ChatClient(Game game, String host, Integer port) throws IOException {
        socket = new Socket(host, port);
        this.game = game;
    }

    public void stop() throws IOException {
        socket.close();
    }


    @Override
    @SneakyThrows
    public void run() {
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
            socket.close();
        }
    }
}
