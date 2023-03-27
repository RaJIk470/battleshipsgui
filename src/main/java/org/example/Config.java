package org.example;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static List<Integer> getDefaultBattleships() {
        return List.of(4, 3, 3, 2, 2, 2, 1, 1, 1, 1);
    }

    public static List<Integer> getReducedBattleships() {
        return List.of(3, 2, 1);
    }
}
