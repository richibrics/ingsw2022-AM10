package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.PawnColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCliDrawersUtilityFunctions {

    @Test
    void idToNameConverter() {
    }

    @Test
    void getStudentColorById() {
        assertThrows(IllegalArgumentException.class, () -> CliDrawersUtilityFunctions.getStudentColorById(ModelConstants.MIN_ID_OF_STUDENT_DISC - 1));
        assertThrows(IllegalArgumentException.class, () -> CliDrawersUtilityFunctions.getStudentColorById(PawnColor.values().length * ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR + 1));
        for (int i = 0; i < PawnColor.values().length; i++) {
            int finalI = i;
            assertEquals(PawnColor.values()[i], assertDoesNotThrow(() -> CliDrawersUtilityFunctions.getStudentColorById(finalI * ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR + 1)));
            int finalI1 = i;
            assertEquals(PawnColor.values()[i], assertDoesNotThrow(() -> CliDrawersUtilityFunctions.getStudentColorById(finalI1 * ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR + ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR)));
        }
    }
}