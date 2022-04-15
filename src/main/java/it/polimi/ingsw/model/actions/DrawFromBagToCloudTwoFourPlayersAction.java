package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;


public abstract class DrawFromBagToCloudTwoFourPlayersAction extends DrawFromBagToCloudAction{
    public DrawFromBagToCloudTwoFourPlayersAction (GameEngine gameEngine) {
        super(gameEngine);
    }


    @Override
    void fromBagToCloud() throws EmptyBagException, TableNotSetException {
        if (getGameEngine().getNumberOfPlayers()==2) {
            for (int i=0; i<2; i++) {
                this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(3,i);
            }
        }
        else
        for (int i=0; i<4; i++) {
            this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(3,i);
        }
    }
}
