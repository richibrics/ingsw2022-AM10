package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.AbstractAssignProfessorAction;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

public class AssignProfessorActionCookEffect extends AssignProfessorActionEffect {

    public AssignProfessorActionCookEffect(GameEngine gameEngine, AbstractAssignProfessorAction assignProfessorAction) {
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
     * @param winningTeam      the team with the player that has the highest number of students of color {@code color} in the
     *                         dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return true if the professor  of color {@code color} has to be moved to a different team, false otherwise
     */

    @Override
    public boolean checkMoveProfessorCondition(PawnColor color, Team winningTeam, Map<Integer, Long> studentsOfPlayer) {
        if (winningTeam.getProfessorTable()
                .stream()
                .filter(professorPawn -> professorPawn.getColor().equals(color)).count() == 0
                && (studentsOfPlayer.values().stream().filter(value -> value == studentsOfPlayer.get(winningTeam.getId())).count() == 1
                || (winningTeam.getPlayers().contains(CommonManager.takePlayerById(this.getGameEngine(), this.getPlayerId()))
                && studentsOfPlayer.get(winningTeam.getId()) != 0)))
            return true;
        else
            return false;
    }
}
