package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientPawnColor {

    /**
     * Checks if it returns the correct ClientPawnColor id.
     */
    @Test
    void getId() {
        assertEquals(0, ClientPawnColor.YELLOW.getId());
        assertEquals(1, ClientPawnColor.BLUE.getId());
        assertEquals(2, ClientPawnColor.GREEN.getId());
        assertEquals(3, ClientPawnColor.RED.getId());
        assertEquals(4, ClientPawnColor.PINK.getId());
    }
}