package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPawnColor {

    @Test
    void getId() {
        assertEquals(0, PawnColor.YELLOW.getId());
        assertEquals(3, PawnColor.RED.getId());
    }

    @Test
    void testEquals() {
        PawnColor color1 = PawnColor.RED;
        PawnColor color2 = PawnColor.RED;
        PawnColor color3 = PawnColor.YELLOW;
        assertTrue(color1.equals(color2));
        assertFalse(color3.equals(color1));
    }

    @Test
    void convertStringToPawnColor() {
        assertEquals(PawnColor.BLUE, assertDoesNotThrow(()->PawnColor.convertStringToPawnColor("bLUe")));
        assertThrows(WrongMessageContentException.class, ()->PawnColor.convertStringToPawnColor("Hello"));
    }
}