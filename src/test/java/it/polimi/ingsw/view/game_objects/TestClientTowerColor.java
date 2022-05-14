package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientTowerColor {

    /**
     * Checks if it returns the correct ClientTowerColor id.
     *
     * @param clientTowerColor to test
     */
    @ParameterizedTest
    @EnumSource(value = ClientTowerColor.class)
    void getId(ClientTowerColor clientTowerColor) {
        assertEquals(clientTowerColor.getId(), ClientTowerColor.values()[clientTowerColor.getId()].getId());
    }
}