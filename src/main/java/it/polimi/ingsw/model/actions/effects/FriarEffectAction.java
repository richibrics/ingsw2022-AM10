package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public class FriarEffectAction extends Action {
    public FriarEffectAction(GameEngine gameEngine) {
        super(12, gameEngine);
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
