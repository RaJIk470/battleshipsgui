package org.example.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.battleship.Battleship;
import org.example.battleship.BattleshipPart;
import org.example.field.BattleshipField;
import org.example.field.CellType;

import java.awt.*;

@Data
@AllArgsConstructor
public class BattleshipShooter {
    private BattleshipField field;

    public boolean shoot(Point point) {
        CellType cellType = field.getCellAt(point);
        return switch (cellType) {
            case EMPTY -> shootAtEmptyCell(point);
            case SHOT, DEAD -> false;
            case BATTLESHIP -> shootAtBattleship(point);
        };
    }

    private boolean shootAtEmptyCell(Point point) {
        field.setCell(point, CellType.SHOT);
        return false;
    }

    private boolean shootAtBattleship(Point point) {
        field.setCell(point, CellType.DEAD);
        Battleship battleship = field.getBattleshipAt(point).get();
        battleship.getPart(point).get().setDead(true);
        battleship.setDead(battleship.getParts().stream().filter(BattleshipPart::isDead).count() == battleship.getLength());
        return true;
    }

}
