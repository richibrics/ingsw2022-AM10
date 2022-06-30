package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

/**
 * Class that manages the action of MailmanEffect.
 */
public class MailmanEffectAction extends Action {
    public MailmanEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MAILMAN_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player.
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens.
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    /**
     * Increment the possible movements of motherNature in the current player turn.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        this.getGameEngine().getAssistantManager().incrementMovementsOfAssistantCardInHand(getPlayerId(), 2);
    }
}
