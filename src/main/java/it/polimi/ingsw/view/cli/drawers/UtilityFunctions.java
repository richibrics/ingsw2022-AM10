package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.view.cli.exceptions.IllegalCharacterIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalProfessorIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientTowerColor;

import static java.lang.Math.floor;

public class UtilityFunctions {

    /**
     * Converts the id of the character card to the name of the card.
     * @param id the id of the character card
     * @return the name of the character card
     * @throws IllegalCharacterIdException if the id of the card is invalid
     */

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

    /**
     * Gets the String which represents a student disc. The string contains the ANSI codes for the color of the student
     * disc. The student disc is represented as follows: "o".
     * @param studentId the id of the student disc
     * @return the String which represents the student disc
     * @throws IllegalStudentIdException if the id of the student disc is invalid
     */

    public static String getRepresentationOfStudentDisc(int studentId) throws IllegalStudentIdException {

        if (studentId >= DrawersConstant.STARTING_ID_YELLOW && studentId <= DrawersConstant.ENDING_ID_YELLOW)
            return DrawersConstant.YELLOW + "o" + DrawersConstant.RESET;
        else if (studentId >= DrawersConstant.STARTING_ID_BLUE && studentId <= DrawersConstant.ENDING_ID_BLUE)
            return DrawersConstant.BLUE + "o" + DrawersConstant.RESET;
        else if (studentId >= DrawersConstant.STARTING_ID_GREEN && studentId <= DrawersConstant.ENDING_ID_GREEN)
            return DrawersConstant.GREEN + "o" + DrawersConstant.RESET;
        else if (studentId >= DrawersConstant.STARTING_ID_RED && studentId <= DrawersConstant.ENDING_ID_RED)
            return DrawersConstant.RED + "o" + DrawersConstant.RESET;
        else if (studentId >= DrawersConstant.STARTING_ID_PINK && studentId <= DrawersConstant.ENDING_ID_PINK)
            return DrawersConstant.PURPLE + "o" + DrawersConstant.RESET;
        else throw new IllegalStudentIdException();
    }

    /**
     * Gets the String which represents a professor pawn. The string contains the ANSI codes for the color of the professor
     * pawn. The professor pawn is represented as follows: "P".
     * @param professorId the id of the professor pawn
     * @return the String which represents the professor pawn
     * @throws IllegalProfessorIdException if the id of the professor pawn is invalid
     */

    public static String getRepresentationOfProfessorPawn(int professorId) throws IllegalProfessorIdException {
        if (professorId == 0)
            return DrawersConstant.YELLOW + "P" + DrawersConstant.RESET;
        else if (professorId == 1)
            return DrawersConstant.BLUE + "P" + DrawersConstant.RESET;
        else if (professorId == 2)
            return DrawersConstant.GREEN + "P" + DrawersConstant.RESET;
        else if (professorId == 3)
            return DrawersConstant.RED + "P" + DrawersConstant.RESET;
        else if (professorId == 4)
            return DrawersConstant.PURPLE + "P" + DrawersConstant.RESET;
        else
            throw new IllegalProfessorIdException();
    }

    /**
     * Gets the String which represents a tower. The string contains the ANSI codes for the color of the tower.
     * The tower is represented as follows: "T".
     * @param clientTowerColor the color of the tower
     * @return the String which represents the tower
     * @throws TowerNotSetException if the tower has not been set
     */

    public static String getRepresentationOfTower(ClientTowerColor clientTowerColor) throws TowerNotSetException {
        if (clientTowerColor.getId() == 0)
            return DrawersConstant.WHITE_BRIGHT + "T" + DrawersConstant.RESET;
        else if (clientTowerColor.getId() == 1)
            return DrawersConstant.BLACK + "T" + DrawersConstant.RESET;
        else if (clientTowerColor.getId() == 2)
            return DrawersConstant.GREY + "T" + DrawersConstant.RESET;
        else
            throw new TowerNotSetException();
    }

    /**
     * Replaces null pointers with " " in {@code template}.
     * @param template the matrix of Strings
     */

    public static void removeNullAndAddSingleSpace(String[][] template) {

        for (int i = 0; i < template.length; i++)
            for (int j = 0; j < template[i].length; j++)
                if (template[i][j] == null)
                    template[i][j] = " ";
    }

    /**
     * Replaces null pointers with "  " in {@code template}.
     * @param template the matrix of Strings
     */

    public static void removeNullAndAddTwoSpaces(String[][] template) {

        for (int i = 0; i < template.length; i++)
            for (int j = 0; j < template[i].length; j++)
                if (template[i][j] == null)
                    template[i][j] = "  ";
    }

    /**
     * Empties a section of the school board which contains pawns or towers starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     * @param heightOfSection the height of the section that must be cleared
     * @param lengthOfSection the length of the section that must be cleared
     * @param pawns a boolean which tells the method if it has to clear a section with pawns (true) or towers (false)
     */

    public static void emptySectionWithPawnsOrTowers(String[][] template, int startingIndexLength, int startingIndexHeight, int heightOfSection, int lengthOfSection, boolean pawns) {
        for (int j = startingIndexLength; j < startingIndexLength + lengthOfSection + 1; j++)
            for (int i = startingIndexHeight; i < startingIndexHeight + heightOfSection; i++) {
                if (pawns)
                    if (template[i][j].contains("o"))
                        template[i][j] = " ";
                    else if (template[i][j].contains("T"))
                        template[i][j] = " ";
            }
    }
}