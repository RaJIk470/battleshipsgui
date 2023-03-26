package org.example.strategy;

import lombok.Data;
import org.example.Game;

@Data
public class NotReadyStrategy implements ActionStrategy{
    @Override
    public void apply(Game game) {

    }
}
