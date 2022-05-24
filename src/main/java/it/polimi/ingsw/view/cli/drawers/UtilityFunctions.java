package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.view.cli.exceptions.IllegalCharacterIdException;

import static java.lang.Math.floor;

public class UtilityFunctions {

    public static String idToNameConverter(int id) throws IllegalCharacterIdException {
        switch (id) {
            case 1:
                return "Friar";
            case 2:
                return "Cook";
            case 3:
                return "Ambassador";
            case 4:
                return "Mailman";
            case 5:
                return "Herbalist";
            case 6:
                return "Centaur";
            case 7:
                return "Jester";
            case 8:
                return "Knight";
            case 9:
                return "Mushroom Hunter";
            case 10:
                return "Minstrel";
            case 11:
                return "Lady";
            case 12:
                return "Thief";
            default:
                throw new IllegalCharacterIdException();
        }
    }

    /**
     * Gets the color of the student using the student id.
     * @param studentId the id of the student
     * @return the color of the student
     */
    public static PawnColor getStudentColorById(int studentId) {
        if(studentId < ModelConstants.MIN_ID_OF_STUDENT_DISC || studentId > PawnColor.values().length * ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR)
            throw new IllegalArgumentException("Student id not valid");
        int colorIndex = (int) floor((studentId-ModelConstants.MIN_ID_OF_STUDENT_DISC)/ ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR);
        return PawnColor.values()[colorIndex];
    }
}
