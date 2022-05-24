package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPawnColor {

    /**
     * Checks if it returns the correct PawnColor id.
     */
    @Test
    void getId() {
        assertEquals(0, PawnColor.YELLOW.getId());
        assertEquals(3, PawnColor.RED.getId());
    }

    /**
     * Checks if it returns the correct color.
     */
    @Test
    void testEquals() {
        PawnColor color1 = PawnColor.RED;
        PawnColor color2 = PawnColor.RED;
        PawnColor color3 = PawnColor.YELLOW;
        assertTrue(color1.equals(color2));
        assertFalse(color3.equals(color1));
    }

    /**
     * Checks if it gets a string and converts it to a PawnColor correctly.
     *
     * @throws WrongMessageContentException if {@code color} is invalid.
     */
    @Test
    void convertStringToPawnColor() {
        assertEquals(PawnColor.BLUE, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("bLUe")));
        assertThrows(WrongMessageContentException.class, () -> PawnColor.convertStringToPawnColor("Hello"));
        assertEquals(PawnColor.RED, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("reD")));
        assertThrows(WrongMessageContentException.class, () -> PawnColor.convertStringToPawnColor("Hello"));
        assertEquals(PawnColor.YELLOW, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("YellOW")));
        assertThrows(WrongMessageContentException.class, () -> PawnColor.convertStringToPawnColor("Hello"));
        assertEquals(PawnColor.GREEN, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("greEn")));
        assertThrows(WrongMessageContentException.class, () -> PawnColor.convertStringToPawnColor("Hello"));
        assertEquals(PawnColor.PINK, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("pInk")));
        assertThrows(WrongMessageContentException.class, () -> PawnColor.convertStringToPawnColor("Hello"));

        // Now use first letter
        assertEquals(PawnColor.BLUE, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("b")));
        assertEquals(PawnColor.RED, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("r")));
        assertEquals(PawnColor.YELLOW, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("y")));
        assertEquals(PawnColor.GREEN, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("g")));
        assertEquals(PawnColor.PINK, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("p")));
        assertEquals(PawnColor.BLUE, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("B")));
        assertEquals(PawnColor.RED, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("R")));
        assertEquals(PawnColor.YELLOW, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("Y")));
        assertEquals(PawnColor.GREEN, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("G")));
        assertEquals(PawnColor.PINK, assertDoesNotThrow(() -> PawnColor.convertStringToPawnColor("P")));
    }
}