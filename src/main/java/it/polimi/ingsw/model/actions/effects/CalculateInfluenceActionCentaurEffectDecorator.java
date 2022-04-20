package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.IslandTile;

import java.util.ArrayList;
import java.util.Map;

public class CalculateInfluenceActionCentaurEffectDecorator extends CalculateInfluenceDecorator {

    public CalculateInfluenceActionCentaurEffectDecorator(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}. When resolving the conquering on
     * {@code islandGroup}, towers do not count towards influence.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    @Override
    public void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception {
        this.getCalculateInfluenceAction().calculateInfluences(influences, islandGroup);
        for (Team team : this.getGameEngine().getTeams())
            for (IslandTile islandTile : islandGroup)
                if (islandTile.hasTower())
                    if (islandTile.getTower().getColor().equals(team.getTeamTowersColor()))
                        influences.put(team.getId(), influences.get(team.getId()) - 1);
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }
}
