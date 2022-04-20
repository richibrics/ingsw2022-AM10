package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public abstract class MailmanEffectAction extends Action {
    public MailmanEffectAction(GameEngine gameEngine){
        super(10,gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception{

    }

    @Override
    public void act() throws Exception {

    }
}
