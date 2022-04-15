package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.Bag;

public abstract class DrawFromBagToCloudThreePlayersAction extends DrawFromBagToCloudAction{
    public DrawFromBagToCloudThreePlayersAction (GameEngine gameEngine) {
        super(gameEngine);
    }

    @Override
    void fromBagToCloud() throws EmptyBagException, TableNotSetException{
        if (getGameEngine().getNumberOfPlayers()==3)
            for (int i=0; i<3; i++) {
            this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(4, i);
        }
    }
}