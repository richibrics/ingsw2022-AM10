package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public abstract class MinstrelEffectAction extends Action {
    public MinstrelEffectAction(GameEngine gameEngine) {
        super(14, gameEngine);
    }



    @Override
    public void setOptions(Map<String, String> options) throws Exception{

    }

    @Override
    public void act() throws Exception {

    }
}
