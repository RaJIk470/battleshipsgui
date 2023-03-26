package org.example.listener;

import org.example.Game;
import org.example.field.BattleshipField;
import org.example.field.BattleshipPlacer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class BattleshipPlacerMouseListener implements MouseListener {
    private BattleshipPlacer battleshipPlacer;
    private BattleshipField battleshipField;
    private Game game;

    public BattleshipPlacerMouseListener(List<Integer> lengths, Game game) {
        this.battleshipField = game.getUserField();
        battleshipPlacer = new BattleshipPlacer(lengths, game.getUserField());
        this.game = game;
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (game.isReadyForBattle()) return;
        int x = mouseEvent.getX() / battleshipField.getCellWidth();
        int y = mouseEvent.getY() / battleshipField.getCellHeight();
        Point point = new Point(x, y);

        if (mouseEvent.getButton() == 1) {
            battleshipPlacer.place(point, false);
        }

        if (mouseEvent.getButton() == 3) {
            if (battleshipField.getBattleshipAt(point).isPresent()) {
                battleshipPlacer.remove(point);
            } else {
                battleshipPlacer.place(point, true);
            }
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
