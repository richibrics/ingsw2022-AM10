package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public class MailmanEffectAction extends Action {
    public MailmanEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MAILMAN_ID, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {
        this.getGameEngine().getRound().getCurrentPlayer();
        if (getPlayerId() == getGameEngine().getRound().getCurrentPlayer())
            this.getGameEngine().getAssistantManager().incrementMovementsOfAssistantCardInHand(getGameEngine().getRound().getCurrentPlayer(), 2);
    }
}
