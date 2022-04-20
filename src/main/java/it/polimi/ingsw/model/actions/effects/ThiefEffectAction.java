package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public abstract class ThiefEffectAction extends Action {
    public ThiefEffectAction(GameEngine gameEngine) {
        super(16, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    @Override
    public void act() throws Exception {

    }
}
