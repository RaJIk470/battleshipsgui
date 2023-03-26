package org.example;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.example.field.BattleshipField;
import org.example.field.CellType;
import org.example.listener.BattleshipFieldMouseListener;
import org.example.listener.BattleshipPlacerMouseListener;
import org.example.net.BattleshipClient;
import org.example.net.BattleshipServer;
import org.example.net.ChatClient;
import org.example.net.ChatServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Getter
public class MainForm extends JFrame {
    private JPanel fields;
    private Game game;
    private Color color;
    private BottomPanel bp;

    @SneakyThrows
    public MainForm(BattleshipField userField, BattleshipField enemyField, Game game) {
        color = new Color(189, 205, 214);
        UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
        setVisible(true);
        setSize(1100, 1000);
        fields = new JPanel();
        fields.add(userField);
        fields.add(enemyField);

        this.game = game;
        this.setLayout(new GridLayout(2, 1));

        add(fields);
        bp = new BottomPanel(color);
        bp.getClientBtn().addActionListener((e) -> {
            try {
                if (!bp.getClientBtn().getText().equals("Stop")) {
                    bp.getClientBtn().setText("Stop");
                    game.setBattleshipUser(new BattleshipClient(game, bp.getIpField().getText(), Game.GAME_PORT));
                    game.setChatUser(new ChatClient(game, bp.getIpField().getText(), Game.GAME_CHAT_PORT));
                    game.setClient(true);
                } else {
                    bp.getClientBtn().setText("Client");
                    game.getBattleshipUser().stop();
                    game.getChatUser().stop();
                    game.reset();
                }
            } catch (Exception ex) {
                bp.getClientBtn().setText("Client");
                MessageBox.infoBox("Connection error", "error");
            }
        });

        bp.getServerBtn().addActionListener((e) -> {
            try {
                if (!bp.getServerBtn().getText().equals("Stop")) {
                    game.setBattleshipUser(new BattleshipServer(game,  Game.GAME_PORT));
                    game.setChatUser(new ChatServer(game, Game.GAME_CHAT_PORT));
                    game.setClient(false);
                    bp.getServerBtn().setText("Stop");
                } else {
                    bp.getServerBtn().setText("Server");
                    game.getBattleshipUser().stop();
                    game.getChatUser().stop();
                    game.reset();
                }
            } catch (Exception ex) {
                bp.getServerBtn().setText("Server");
                MessageBox.infoBox("Failed to create server", "error");
            }

        });

        bp.getSendBtn().addActionListener((e) -> {
            try {
                String msg = bp.getMsgField().getText();
                bp.getMsgField().setText("");
                game.getChatUser().addMessage(msg);
            } catch (Exception ex) {

            }
        });

        bp.getReadyBtn().addActionListener((e) -> {
            if (game.isReadyForBattle()) {
                game.setReadyForBattle(false);
                bp.getReadyBtn().setText("Ready");
            } else if (game.getUserField().isFilled())  {
                game.setReadyForBattle(true);
                bp.getReadyBtn().setText("Not ready");
            } else {
                MessageBox.infoBox("You must place all the ships", "Error");
            }
            System.out.println(game.isReadyForBattle());
        });

        bp.getApplyBtn().addActionListener((e) -> {
            game.setUsername(bp.getUsernameField().getText());
        });

        add(bp);
        //fields.setBackground(new Color(238, 233, 218));
        fields.setBackground(color);
    }
}