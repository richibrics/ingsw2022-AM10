package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.PawnColor;

import java.util.Map;

public class AssignProfessorsAction extends AbstractAssignProfessorAction {

    public AssignProfessorsAction (GameEngine gameEngine) { super(gameEngine); }

    /**
     * Checks if the professor  of color {@code color} has to be moved to a different team.
     * @param color the color of the professor pawn
     * @param winningTeam the team with the player that has the highest number of students of color {@code color} in the
     *                    dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return true if the professor  of color {@code color} has to be moved to a different team, false otherwise
     */

    @Override
    public boolean checkMoveProfessorCondition (PawnColor color , Team winningTeam, Map <Integer, Long> studentsOfPlayer) {
        if(winningTeam.getProfessorTable()
                .stream()
                .filter(professorPawn -> professorPawn.getColor().equals(color)).count() == 0
                        && studentsOfPlayer.values().stream().filter(value -> value == studentsOfPlayer.get(winningTeam.getId())).count() == 1)
            return true;
        else
            return false;
    }
}