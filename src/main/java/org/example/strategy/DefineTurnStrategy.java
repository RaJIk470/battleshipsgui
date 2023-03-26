package org.example.strategy;

import lombok.Data;
import org.example.Game;

@Data
public class DefineTurnStrategy implements ActionStrategy {
    private boolean isUserTurn;
    public DefineTurnStrategy(boolean isUserTurn) {
        this.isUserTurn = isUserTurn;
    }
    @Override
    public void apply(Game game) {
        game.setUserTurn(isUserTurn);
    }
}
