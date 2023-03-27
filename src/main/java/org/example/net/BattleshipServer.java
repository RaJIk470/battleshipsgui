package org.example.net;

import lombok.SneakyThrows;
import org.example.Game;
import org.example.MessageBox;
import org.example.field.CellType;
import org.example.strategy.*;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;

public class BattleshipServer extends BattleshipUser implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private Game game;

    public BattleshipServer(Game game, int port) throws Exception {
        serverSocket = new ServerSocket(port);
        this.game = game;
    }

    public void stop() throws IOException {
        serverSocket.close();
        socket.close();
    }

    @Override
    public void run() {
        socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.setConnected(true);
        MessageBox.infoBox("Client accepted", "Accept");
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            while (game.isConnected()) {
                //inputStream = new BufferedInputStream(socket.getInputStream());
                //outputStream = new BufferedOutputStream(socket.getOutputStream());
                while (!game.isGameStarted()) {
                    if (game.isReadyForBattle()) {
                        writeStrategy(new CheckIfIsReadyStrategy());
                        ActionStrategy strategy = readStrategy();
                        if (strategy instanceof ReadyStrategy)
                            game.setGameStarted(true);
                    }
                    Thread.sleep(1000);
                }
                SecureRandom random = new SecureRandom();
                boolean isUserTurn = random.nextBoolean();
                if (isUserTurn) {
                    MessageBox.infoBox("You are first", "First turn");
                } else {
                    MessageBox.infoBox("You are Second", "Second turn");
                }
                game.setUserTurn(isUserTurn);
                writeStrategy(new DefineTurnStrategy(!isUserTurn));

                MessageBox.infoBox("Game is started", "Start");
                while (game.isGameStarted()) {
                    if (!game.isUserTurn()) {
                        ActionStrategy s = readStrategy();
                        if (s instanceof ShootStrategy) {
                            s.apply(game);
                            if (game.getUserField().isAllDead()) {
                                writeStrategy(new DefeatedStrategy());
                                MessageBox.infoBox("You are the loser :D", "Lose");
                                game.reset();
                                stop();
                            } else {
                                writeStrategy(new DefineTurnStrategy(!game.isUserTurn()));
                            }
                        }
                    } else {
                        while (socket.isConnected() && shootPoint == null || game.getEnemyField().getCellAt(shootPoint) == CellType.DEAD) {
                            System.out.println(shootPoint);
                            Thread.sleep(100);
                        }
                        writeStrategy(new ShootStrategy(shootPoint));
                        ActionStrategy s = readStrategy();
                        if (s instanceof DefineTurnStrategy) {
                            boolean prev = game.isUserTurn();
                            s.apply(game);
                            if (prev && game.isUserTurn()) game.getEnemyField().setCell(shootPoint, CellType.DEAD);
                            else game.getEnemyField().setCell(shootPoint, CellType.SHOT);
                        }

                        if (s instanceof DefeatedStrategy) {
                            MessageBox.infoBox("You are the winner!", "Win");
                            game.reset();
                            stop();
                        }

                        shootPoint = null;
                    }
                }
            }
        } catch (Exception e) {
            MessageBox.infoBox("Client disconnected", "Disconnected");
            game.reset();
        } finally {
            try {
                serverSocket.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
