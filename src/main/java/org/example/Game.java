package org.example;

import lombok.Data;
import org.example.field.BattleshipField;
import org.example.field.CellType;
import org.example.listener.BattleshipFieldMouseListener;
import org.example.listener.BattleshipPlacerMouseListener;
import org.example.net.BattleshipUser;
import org.example.net.ChatUser;

@Data
public class Game {
    public static final Integer GAME_PORT = 10931;
    public static final Integer GAME_CHAT_PORT = 10932;
    private BattleshipField userField;
    private BattleshipField enemyField;
    private MainForm mainForm;

    private boolean isUserTurn = false;
    private boolean isReadyForBattle = false;
    private boolean isGameStarted = false;
    private boolean isClient = false;
    private boolean isConnected = false;

    private BattleshipUser battleshipUser;
    private ChatUser chatUser;

    private String username = "Anonymous";

    private BattleshipFieldMouseListener battleshipFieldMouseListener;
    private BattleshipPlacerMouseListener battleshipPlacerMouseListener;

    public Game() {
        userField = new BattleshipField(500, 500);
        enemyField = new BattleshipField(500, 500);
        battleshipFieldMouseListener = new BattleshipFieldMouseListener(this);
        battleshipPlacerMouseListener = new BattleshipPlacerMouseListener(Config.getReducedBattleships(), this);
        enemyField.addMouseListener(battleshipFieldMouseListener);
        userField.addMouseListener(battleshipPlacerMouseListener);

        mainForm = new MainForm(userField, enemyField, this);
    }

    public void setBattleshipUser(BattleshipUser battleshipUser) {
        try {
            if (this.battleshipUser != null)
                this.battleshipUser.stop();
        } catch (Exception e) {

        }
        this.battleshipUser = battleshipUser;
        Thread thread = new Thread(this.battleshipUser);
        thread.start();
    }

    public void setChatUser(ChatUser chatUser) {
        try {
            if (this.chatUser != null)
                this.chatUser.stop();
        } catch (Exception e) {

        }
        this.chatUser = chatUser;
        Thread thread = new Thread(this.chatUser);
        thread.start();
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    public void reset() {
        isUserTurn = false;
        isReadyForBattle = false;
        isGameStarted = false;
        isClient = false;
        isConnected = false;
        mainForm.getBp().getServerBtn().setText("Server");
        mainForm.getBp().getClientBtn().setText("Client");
        mainForm.getBp().getReadyBtn().setText("Ready");
        mainForm.getBp().getChatArea().setText("");

        userField.clearField();
        enemyField.clearField();

        try {
            chatUser.stop();
            battleshipUser.stop();
        } catch (Exception e) {

        }

        enemyField.removeMouseListener(battleshipFieldMouseListener);
        userField.removeMouseListener(battleshipPlacerMouseListener);
        battleshipFieldMouseListener = new BattleshipFieldMouseListener(this);
        battleshipPlacerMouseListener = new BattleshipPlacerMouseListener(Config.getReducedBattleships(), this);
        enemyField.addMouseListener(battleshipFieldMouseListener);
        userField.addMouseListener(battleshipPlacerMouseListener);

        userField.repaint();
        enemyField.repaint();
        mainForm.repaint();
    }
}
