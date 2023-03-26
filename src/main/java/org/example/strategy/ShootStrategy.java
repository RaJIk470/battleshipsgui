package org.example.strategy;

import lombok.Data;
import org.example.Game;
import org.example.field.BattleshipShooter;
import org.example.field.BattleshipField;

import java.awt.*;

@Data
public class ShootStrategy implements ActionStrategy {
    private Point point;
    public ShootStrategy(Point point) {
        this.point = point;
    }
    @Override
    public void apply(Game game) {
        BattleshipShooter battleshipShooter = new BattleshipShooter(game.getUserField());
        game.setUserTurn(!battleshipShooter.shoot(point));
    }
}
