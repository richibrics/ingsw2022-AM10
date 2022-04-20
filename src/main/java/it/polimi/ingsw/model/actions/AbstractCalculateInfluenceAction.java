package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.IslandTile;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractCalculateInfluenceAction extends Action {

    public AbstractCalculateInfluenceAction(GameEngine gameEngine) {
        super(7, gameEngine);
    }

    public abstract void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception;

    @Override
    void modifyRound() throws Exception{

    }
}
