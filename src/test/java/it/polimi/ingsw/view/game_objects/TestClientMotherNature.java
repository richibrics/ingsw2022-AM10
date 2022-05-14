package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientMotherNature {

    /**
     * Creates a ClientMotherNature and it checks if it returns the correct parameter.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void getIsland(int value) {
        ClientMotherNature clientMotherNature = new ClientMotherNature(value);
        assertEquals(clientMotherNature.getIsland(), value);
    }
}