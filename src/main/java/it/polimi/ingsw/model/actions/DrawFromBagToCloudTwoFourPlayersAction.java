package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;


public class DrawFromBagToCloudTwoFourPlayersAction extends DrawFromBagToCloudAction {
    public DrawFromBagToCloudTwoFourPlayersAction(GameEngine gameEngine) {
        super(gameEngine);
    }


    @Override
    void fromBagToCloud() throws EmptyBagException, TableNotSetException {
        if (getGameEngine().getNumberOfPlayers() == ModelConstants.TWO_PLAYERS) {
            for (int i = 1; i <= ModelConstants.TWO_PLAYERS; i++) {
                this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_TWO_PLAYERS_STUDENTS_NUMBER, i);
            }
        } else
            for (int i = 1; i <= ModelConstants.FOUR_PLAYERS; i++) {
                this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_FOUR_PLAYERS_STUDENTS_NUMBER, i);
            }
    }
}
