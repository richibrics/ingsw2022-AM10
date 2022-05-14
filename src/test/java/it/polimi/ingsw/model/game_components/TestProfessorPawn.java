package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProfessorPawn {

    /**
     * Creates a ProfessorPawn and checks if the get returns the correct color.
     *
     * @param color to test.
     */
    @ParameterizedTest
    @EnumSource(PawnColor.class)
    public void testGetColor(PawnColor color) {
        ProfessorPawn professorPawn = new ProfessorPawn(color);
        assertEquals(professorPawn.getColor(), color);
    }
}
