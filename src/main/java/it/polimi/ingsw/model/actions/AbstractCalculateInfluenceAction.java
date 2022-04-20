package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.IslandTile;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractCalculateInfluenceAction extends Action {

    public AbstractCalculateInfluenceAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, gameEngine);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    public abstract void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception;

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRound() throws Exception {
        ArrayList<Integer> possibleActions = new ArrayList<>();
        possibleActions.add(3);
        possibleActions.add(6);
        this.getGameEngine().getRound().setPossibleActions(possibleActions);
    }
}
