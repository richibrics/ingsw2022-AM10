package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.cli.exceptions.IllegalProfessorIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientTowerColor;

public class IdToColorConverter {

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
}
