package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public class MailmanEffectAction extends Action {
    public MailmanEffectAction(GameEngine gameEngine) {
        super(10, gameEngine);
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
