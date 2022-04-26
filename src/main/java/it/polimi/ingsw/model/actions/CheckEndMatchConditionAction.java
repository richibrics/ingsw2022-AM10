package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckEndMatchConditionAction extends Action {
    public CheckEndMatchConditionAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_CHECK_END_MATCH_CONDITION_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * @param options additional information for act method
     * @throws Exception if the required key is not provided
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        this.getGameEngine().getActionManager().executeAction(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_ID, -1, new HashMap<>());
    }

    /**
     * Checks if a team has no towers left.
     * @return the list of winners in the condition is satisfied, null otherwise
     * @throws Exception if something bad happens
     */

    public Integer[] checkTowersCondition() throws Exception {
        for (Team team : this.getGameEngine().getTeams())
            if (team.getTowers().size() == 0) {
                Integer[] winner = {team.getId()};
                return winner;
            }
        return null;
    }

    /**
     * Checks if the number of island groups equals the minimum number of island groups.
     * @return the list of winners in the condition is satisfied, null otherwise
     * @throws Exception if something bad happens
     */

    public Integer[] checkIslandGroupsCondition() throws Exception {
        if (this.getGameEngine().getTable().getIslandTiles().size() == ModelConstants.MIN_NUMBER_OF_ISLAND_GROUPS) {
            return this.findWinner();
        }
        else
            return null;
    }

    /**
     * Checks if the bag is empty
     * @return the list of winners in the condition is satisfied, null otherwise
     * @throws Exception if something bad happens
     */

    public Integer[] noStudentsInBagCondition() throws Exception {
        if (this.getGameEngine().getTable().getBag().getNumberOfStudents() == 0)
            return this.findWinner();
        else
            return null;
    }

    /**
     * Checks if a player has no assistant cards.
     * @return the list of winners in the condition is satisfied, null otherwise
     * @throws Exception if something bad happens
     */

    public Integer[] noAssistantCardsCondition() throws Exception {
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                if (player.getWizard().getAssistantCards().size() == 0)
                    return this.findWinner();
        return null;
    }

    /**
     * Finds the winner of the game. The player who has built the most towers on islands wins the game. In case of a tie,
     * the player who controls the most professors wins the game.
     * @return the list of winners
     * @throws Exception if something bad happens
     */

    public Integer[] findWinner() throws Exception {
        int minTowersLeft = ModelConstants.MAX_NUMBER_OF_TOWERS;
        ArrayList<Team>  possibleWinners = new ArrayList<>();

        /* Find the possible winners */
        /* First criterion: number of towers left */
        for (Team team : this.getGameEngine().getTeams()) {
            int numOfTowers = team.getTowers().size();
            if (numOfTowers == minTowersLeft)
                possibleWinners.add(team);
            else if (numOfTowers < minTowersLeft) {
                possibleWinners.clear();
                possibleWinners.add(team);
                minTowersLeft = numOfTowers;
            }
        }

        if (possibleWinners.size() == 1) {
            return possibleWinners.stream().map(team -> team.getId()).toList().toArray(new Integer[1]);
        }

        else {
            /* Second criterion: number of professors */
            int maxNumOfProfessors = -1;
            ArrayList<Team>  winners = new ArrayList<>();
            for (Team team : possibleWinners) {
                int numOfProfessors = team.getProfessorTable().size();
                if (numOfProfessors == maxNumOfProfessors)
                    winners.add(team);
                else if (numOfProfessors > maxNumOfProfessors) {
                    winners.clear();
                    winners.add(team);
                    maxNumOfProfessors = numOfProfessors;
                }
            }
            return winners.stream().map(team -> team.getId()).toList().toArray(new Integer[1]);
        }
    }

    /**
     * Communicates the winner of the game.
     * @param teamIds the ids of the winning teams
     * @throws Exception if something bad happens
     */

    //TODO
    public void communicateWinner(Integer[] teamIds) throws Exception {

    }

    /**
     * Checks if one of the game-ending conditions is satisfied. Ends the game if a condition is satisfied.
     * @throws Exception if something bad happens
     */

    @Override
    public void act() throws Exception {
        Integer[] winners = null;
        winners = this.checkTowersCondition();
        if (winners != null)
            this.communicateWinner(winners);
        winners = this.checkIslandGroupsCondition();
        if (winners != null)
            this.communicateWinner(winners);
        winners = this.noStudentsInBagCondition();
        if (winners != null)
            this.communicateWinner(winners);
        winners = this.noAssistantCardsCondition();
        if (winners != null)
            this.communicateWinner(winners);
    }
}