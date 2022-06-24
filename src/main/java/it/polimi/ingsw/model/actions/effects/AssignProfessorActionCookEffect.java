package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;
import java.util.Objects;

public class AssignProfessorActionCookEffect extends AssignProfessorActionEffect {

    public AssignProfessorActionCookEffect(GameEngine gameEngine, Action assignProfessorAction) {
        super(gameEngine, assignProfessorAction);
    }

    /**
     * Gets the id of the player with the highest number of students of a certain color in the dining room. In case of a draw,
     * if one of the players with the highest number of students of the required color is {@code this.playerId}, returns the id
     * of the team with the player {@code this.playerId}.
     *
     * @param studentsOfPlayer the map with playerId - number of students of the required color in the dining room
     * @return the id of the team with the player with the highest number of students of a certain color in the dining room
     */

    @Override
    public int getKey(Map<Integer, Long> studentsOfPlayer) {
        int key = super.getKey(studentsOfPlayer);
        if (studentsOfPlayer.containsKey(CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId())))
            if (studentsOfPlayer.get(CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId())) == studentsOfPlayer.get(key))
                key = CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId());
        return key;
    }

    /**
     * Checks if the professor  of color {@code color} has to be moved to a different team.
     *
     * @param color            the color of the professor pawn
     * @param winningPlayer      the player that has the highest number of students of color {@code color} in the
     *                         dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return the id of the team which should receive the professor pawn and an integer which equals 1 if the professor
     * of color {@code color} has to be moved to a different team, 0 otherwise
     */

    @Override
    public int[] checkMoveProfessorCondition(PawnColor color, Player winningPlayer, Map<Integer, Long> studentsOfPlayer) {

        // The player has the same number of students (!= 0) of the winning player and does not have the professor pawn
        if (studentsOfPlayer.get(winningPlayer.getPlayerId()) != 0 &&
                Objects.equals(studentsOfPlayer.get(this.getPlayerId()), studentsOfPlayer.get(winningPlayer.getPlayerId())) &&
                CommonManager.takeTeamById(this.getGameEngine(), CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId())).getProfessorTable()
                        .stream().noneMatch(professorPawn -> professorPawn.getColor().equals(color)))
            return new int[]{CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId()), 1};

        // The winning player does not have the professor pawn and is the only winner
        else if (CommonManager.takeTeamById(this.getGameEngine(), CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), winningPlayer.getPlayerId())).getProfessorTable()
                .stream().noneMatch(professorPawn -> professorPawn.getColor().equals(color))
                && studentsOfPlayer.values().stream().filter(value -> Objects.equals(value, studentsOfPlayer.get(winningPlayer.getPlayerId()))).count() == 1)
            return new int[]{CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), winningPlayer.getPlayerId()), 1};

        // No change should take place
        else
            return new int[]{-1, 0};
    }
}
