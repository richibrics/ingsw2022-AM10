package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.Map;

public abstract class FromCloudTileToEntranceAction extends Action {
    public FromCloudTileToEntranceAction(GameEngine gameEngine) {
        super(6, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    @Override
    public void act() throws Exception {

    }
}
