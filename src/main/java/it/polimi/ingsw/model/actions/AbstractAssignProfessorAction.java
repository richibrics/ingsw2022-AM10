package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAssignProfessorAction extends Action {

    public AbstractAssignProfessorAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_ASSIGN_PROFESSORS_ID, gameEngine);
    }

    /**
     * Gets the id of the player with the highest number of students of a certain color in the dining room.
     *
     * @param studentsOfPlayer the map with playerId - number of students of the required color in the dining room
     * @return the id of the player with the highest number of students of a certain color in the dining room
     */

    public int getKey(Map<Integer, Long> studentsOfPlayer) {
        return studentsOfPlayer.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    /**
     * Checks if the professor  of color {@code color} has to be moved to a different team.
     *
     * @param color            the color of the professor pawn
     * @param player      the player that has the highest number of students of color {@code color} in the
     *                         dining room
     * @param studentsOfPlayer the map with playerId - number of students of color {@code color} in the dining room
     * @return true if the professor  of color {@code color} has to be moved to a different team, false otherwise
     */

    public abstract boolean checkMoveProfessorCondition(PawnColor color, Player player, Map<Integer, Long> studentsOfPlayer);

    /**
     * Implements the behaviour of the action.
     */

    @Override
    public void act() throws Exception {
        for (PawnColor color : PawnColor.values()) {
            Map<Integer, Long> studentsOfPlayer = new HashMap<>();
            for (Team team : this.getGameEngine().getTeams())
                for (Player player : team.getPlayers())
                    studentsOfPlayer.put(player.getPlayerId(), player.getSchoolBoard().getDiningRoomColor(color).stream().count());

            int key = this.getKey(studentsOfPlayer);
            Team winningTeam = CommonManager.takeTeamById(this.getGameEngine(), CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), key));
            if (this.checkMoveProfessorCondition(color, CommonManager.takePlayerById(this.getGameEngine(), key), studentsOfPlayer)) {
                if (this.getGameEngine().getTable().getAvailableProfessorPawns().stream().filter(professorPawn -> professorPawn.getColor().equals(color)).count() == 1)
                    winningTeam.addProfessorPawn(this.getGameEngine().getTable().popProfessorPawn(color));
                else
                    for (Team team : this.getGameEngine().getTeams())
                        if (team.getProfessorTable().stream().filter(professorPawn -> professorPawn.getColor().equals(color)).count() == 1) {
                            this.getGameEngine().getSchoolPawnManager().moveProfessor(winningTeam.getId(), team.getId(), color);
                            break;
                        }
            }
        }
    }

    /**
     * Sets the options. Option represents additional information used by the act method.
     *
     * @param options additional information for act method
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * <p>
     * After this action the player will move mother nature, so I add that action to the round action list.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        ArrayList<Integer> possibleActions = getGameEngine().getRound().getPossibleActions();
        possibleActions.add(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID);
        this.getGameEngine().getRound().setPossibleActions(possibleActions);
    }
}
