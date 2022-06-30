package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that manages the action of DrawFromBagToCloud.
 */
public abstract class DrawFromBagToCloudAction extends Action {

    public DrawFromBagToCloudAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_ID, gameEngine);
    }


    public void setOptions(Map<String, String> options) throws Exception {

    }

    @Override
    public void act() throws Exception {
        this.fromBagToCloud();
    }

    abstract void fromBagToCloud() throws EmptyBagException, TableNotSetException;

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * After this action the players will choose the assistant, with the same order already present in the round.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        // Set next action (select assistant card): I don't touch the order
        ArrayList<Integer> nextActions = getGameEngine().getRound().getPossibleActions();
        nextActions.add(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID);
        this.getGameEngine().getRound().setPossibleActions(nextActions);
    }

}
