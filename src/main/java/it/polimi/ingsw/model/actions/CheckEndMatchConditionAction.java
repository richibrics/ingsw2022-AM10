package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.Map;

public class CheckEndMatchConditionAction extends Action {
    public CheckEndMatchConditionAction(GameEngine gameEngine) {
        super(9, gameEngine);
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {

    }
}
