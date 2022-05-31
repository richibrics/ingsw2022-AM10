package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

public class AssignProfessorsAction extends AbstractAssignProfessorAction {

    public AssignProfessorsAction(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Checks if the professor  of color {@code color} has to be moved to a different team.
     *
     * @param color            the color of the professor pawn
     * @param winningPlayer      the player that has the highest number of students of color {@code color} in the
     *                         dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return true if the professor  of color {@code color} has to be moved to a different team, false otherwise
     */

    @Override
    public boolean checkMoveProfessorCondition(PawnColor color, Player winningPlayer, Map<Integer, Long> studentsOfPlayer) {
        return CommonManager.takeTeamById(this.getGameEngine(), CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), winningPlayer.getPlayerId())).getProfessorTable()
                .stream()
                .filter(professorPawn -> professorPawn.getColor().equals(color)).count() == 0
                && studentsOfPlayer.values().stream().filter(value -> value == studentsOfPlayer.get(winningPlayer.getPlayerId())).count() == 1;
    }
}