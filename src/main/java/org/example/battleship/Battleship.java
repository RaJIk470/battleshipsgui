package org.example.battleship;

import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Battleship {
    private int length;
    private ArrayList<BattleshipPart> parts;
    private boolean isDead = false;

    public Battleship(List<BattleshipPart> parts) {
        length = parts.size();
        this.parts = new ArrayList<>(parts);
    }

    public Optional<BattleshipPart> getPart(Point point) {
        return parts.stream().filter((bp) -> bp.getPosition().equals(point)).findFirst();
    }
}
