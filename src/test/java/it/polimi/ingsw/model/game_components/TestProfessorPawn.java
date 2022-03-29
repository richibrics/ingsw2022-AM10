package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProfessorPawn {

    @ParameterizedTest
    @EnumSource(PawnColor.class)
    public void testGetColor(PawnColor color)
    {
        ProfessorPawn professorPawn = new ProfessorPawn(color);
        assertEquals(professorPawn.getColor(), color);
    }
}
