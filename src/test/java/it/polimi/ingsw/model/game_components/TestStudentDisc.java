package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class TestStudentDisc {

    /**
     * Creates a StudentDisc and checks if it returns the correct id and the correct color.
     *
     * @param color to test.
     */
    @ParameterizedTest
    @EnumSource(PawnColor.class)
    public void testGetColor(PawnColor color)
    {
        StudentDisc studentDisc = new StudentDisc(0,color);
        assertEquals(studentDisc.getColor(), color);
        assertEquals(studentDisc.getColor().getId(), color.getId());
    }

    /**
     * Creates a StudentDisc and checks if it returns the correct id of StudentDisc.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 900000, Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        assertEquals(new StudentDisc(value,PawnColor.BLUE).getId(), value);
        assertNotEquals(new StudentDisc(value,PawnColor.BLUE).getId(), value+1);
    }
}
