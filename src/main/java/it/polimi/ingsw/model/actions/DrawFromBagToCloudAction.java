package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

public abstract class DrawFromBagToCloudAction extends Action{

    public DrawFromBagToCloudAction(GameEngine gameEngine) {
        super (1,gameEngine);
        }

    public void setPlayerId(int playerId) {

    }


    public void setOptions(String options) {

    }

    @Override
    public void act() throws Exception {
        this.fromBagToCloud();
    }

    abstract void fromBagToCloud() throws EmptyBagException, TableNotSetException;

}
