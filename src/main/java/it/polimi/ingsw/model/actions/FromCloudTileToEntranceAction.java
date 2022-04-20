package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.Map;

public class FromCloudTileToEntranceAction extends Action {
    public FromCloudTileToEntranceAction(GameEngine gameEngine) {
        super(6, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     */
    @Override
    public void modifyRound() throws Exception {

    }

    @Override
    public void act() throws Exception {

    }
}
