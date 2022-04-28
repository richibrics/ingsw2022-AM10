package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class TestTowerColor {

    @ParameterizedTest
    @EnumSource(value = TowerColor.class)
    void getId(TowerColor towerColor) {
        assertEquals(towerColor.getId(), TowerColor.values()[towerColor.getId()].getId());
    }
}