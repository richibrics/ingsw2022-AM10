package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTowerColor {

    /**
     * Checks if it returns the correct id of TowerColor
     */
    @ParameterizedTest
    @EnumSource(value = TowerColor.class)
    void getId(TowerColor towerColor) {
        assertEquals(towerColor.getId(), TowerColor.values()[towerColor.getId()].getId());
    }
}