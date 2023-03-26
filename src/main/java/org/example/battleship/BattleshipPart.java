package org.example.battleship;

import lombok.Data;

import java.awt.*;

@Data
public class BattleshipPart {
    private Point position;
    private boolean isDead;

    public BattleshipPart(Point point) {
        position = point;
    }
}
