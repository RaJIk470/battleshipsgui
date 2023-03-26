package org.example.net;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.Game;
import org.example.MessageBox;
import org.example.field.CellType;
import org.example.strategy.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Data
public class BattleshipClient extends BattleshipUser implements Runnable {
    private Socket socket;
    private Game game;
    public BattleshipClient(Game game, String host, int port) throws Exception {
        socket = new Socket(host, port);
        game.setConnected(true);
        MessageBox.infoBox("Successfully connection to the server", "Success");
        //inputStream = new BufferedInputStream(socket.getInputStream());
        //outputStream = new BufferedOutputStream(socket.getOutputStream());
        this.game = game;
    }

    public void stop() throws IOException {
        socket.close();
    }

    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            while (socket.isConnected()) {
                while (!game.isGameStarted()) {
                    ActionStrategy strategy = readStrategy();
                    if (strategy instanceof CheckIfIsReadyStrategy) {
                        writeStrategy(game.isReadyForBattle() ? new ReadyStrategy() : new NotReadyStrategy());
                        game.setGameStarted(game.isReadyForBattle());
                    }
                }

                ActionStrategy strategy = readStrategy();
                if (strategy instanceof DefineTurnStrategy) strategy.apply(game);

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
            MessageBox.infoBox("Server Stopped", "Disconnected");
            game.reset();
        } finally {
            try {
                objectInputStream.close();
                socket.close();
                objectOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
