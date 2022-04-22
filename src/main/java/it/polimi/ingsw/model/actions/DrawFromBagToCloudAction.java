package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

import java.util.Map;

public abstract class DrawFromBagToCloudAction extends Action {

    public DrawFromBagToCloudAction(GameEngine gameEngine) {
        super(1, gameEngine);
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
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

}
