package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

public abstract class CheckEndMatchConditionAction extends Action{
    public CheckEndMatchConditionAction(GameEngine gameEngine) {
        super(9, gameEngine);
    }

    @Override
    public void setPlayerId(int playerId) {

    }

    @Override
    public void setOptions(String options) {

    }

    @Override
    public void act() throws Exception {

    }
}
