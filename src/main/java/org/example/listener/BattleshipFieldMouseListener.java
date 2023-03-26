package org.example.listener;

import org.example.Game;
import org.example.field.BattleshipField;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BattleshipFieldMouseListener implements MouseListener {
    private Game game;
    public BattleshipFieldMouseListener(Game game) {
        this.game = game;
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game.isGameStarted() && game.isUserTurn()) {
            BattleshipField field = game.getEnemyField();
            int i = mouseEvent.getX() / field.getCellWidth();
            int j = mouseEvent.getY() / field.getCellHeight();
            System.out.println("Clicked " + i + " " + j);

            game.getBattleshipUser().setShootPoint(new Point(i, j));
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
