package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.Map;

public class CalculateInfluenceActionKnightEffectDecorator extends CalculateInfluenceDecorator {

    public CalculateInfluenceActionKnightEffectDecorator(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}. Increments by 2 the influence of the team with
     * the player {@code this.playerId}.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    @Override
    public void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception {
        this.getCalculateInfluenceAction().calculateInfluences(influences, islandGroup);
        influences.put(CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId()), influences.get(CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId())) + 2);
    }

    /**
     * Calculates the influence that each team has on the island group containing the mother nature pawn, checks if
     * changes are needed and makes the required changes to the island group.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void act() throws Exception {
        this.getCalculateInfluenceAction().act();
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }
}
