package org.example.field;

import org.example.battleship.Battleship;
import org.example.field.BattleshipField;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BattleshipPlacer {
    private Deque<Integer> placingStack;
    private BattleshipField field;

    public BattleshipPlacer(List<Integer> lengths, BattleshipField field) {
        placingStack = new ArrayDeque<>(lengths);
        this.field = field;
    }

    public int pollNext() {
        return placingStack.pollFirst();
    }

    public int getNext() {
        return placingStack.getFirst();
    }

    public void push(int length) {
        placingStack.push(length);
    }

    public boolean isEmpty() {
        return placingStack.isEmpty();
    }

    public void place(Point point, boolean isVertical) {
        if (!isEmpty() && isCorrectPosition(point, getNext(), isVertical)) {
            int nextLength = pollNext();
            field.addBattleship(point, nextLength, isVertical);
        }

        field.setFilled(isEmpty());
    }

    public boolean hasShipsNear(Point point, int length, boolean isVertical) {
        int len = field.getFieldLength();
        for (int i = 0; i < length; i++) {
            Point pt = new Point(point);
            if (isVertical) pt.translate(0, i); else pt.translate(i, 0);

            int startX = pt.x == 0 ? pt.x : pt.x - 1;
            int startY = pt.y == 0 ? pt.y : pt.y - 1;
            int endX = pt.x == len - 1 ? pt.x : pt.x + 1;
            int endY = pt.y == len - 1 ? pt.y : pt.y + 1;

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    if (field.getBattleshipAt(new Point(x, y)).isPresent()) return true;
                }
            }
        }
        return false;
    }

    public boolean isCorrectPosition(Point point, int length, boolean isVertical) {
        int len = field.getFieldLength();
        if (point.x >= 0 && point.y >= 0) {
            if (!isVertical && point.x + length - 1 < len || isVertical && point.y + length - 1 < len)
                if (!hasShipsNear(point, length, isVertical))
                    return true;
        }

        return false;
    }

    public void remove(Point point) {
        Optional<Battleship> optionalBattleship = field.removeBattleship(point);
        field.repaint();
        if (optionalBattleship.isPresent()) {
            Battleship battleship = optionalBattleship.get();
            push(battleship.getLength());
        }

        field.setFilled(isEmpty());
    }
}
