package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

public abstract class LadyEffectAction extends Action {
    public LadyEffectAction(GameEngine gameEngine) {
        super(15, gameEngine);
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