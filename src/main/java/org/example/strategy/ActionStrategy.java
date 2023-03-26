package org.example.strategy;

import lombok.Data;
import org.example.Game;
import org.example.field.BattleshipField;

import java.io.Serializable;

public interface ActionStrategy extends Serializable {
    void apply(Game game);
}
