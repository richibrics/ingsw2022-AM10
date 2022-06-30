package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

/**
 * Class that checks if a specific ProfessorPawn has to be moved to a different team.
 */
public class AssignProfessorsAction extends AbstractAssignProfessorAction {

    public AssignProfessorsAction(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Checks if the professor  of color {@code color} has to be moved to a different team.
     *
     * @param color            the color of the professor pawn
     * @param winningPlayer    the player that has the highest number of students of color {@code color} in the
     *                         dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return the id of the team which should receive the professor pawn and an integer which equals 1 if the professor
     * of color {@code color} has to be moved to a different team, 0 otherwise
     */

    @Override
    public int[] checkMoveProfessorCondition(PawnColor color, Player winningPlayer, Map<Integer, Long> studentsOfPlayer) {

        // The winning player does not have the professor pawn and is the only winner
        if (CommonManager.takeTeamById(this.getGameEngine(), CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), winningPlayer.getPlayerId())).getProfessorTable()
                .stream().noneMatch(professorPawn -> professorPawn.getColor().equals(color))
                && studentsOfPlayer.values().stream().filter(value -> value == studentsOfPlayer.get(winningPlayer.getPlayerId())).count() == 1)
            return new int[]{CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), winningPlayer.getPlayerId()), 1};

        else
            return new int[]{-1, 0};
    }
}