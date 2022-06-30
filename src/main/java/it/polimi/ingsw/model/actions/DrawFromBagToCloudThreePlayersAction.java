package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

/**
 * Class that draws the students from bag and push them to cloud in a 3-player game.
 */
public class DrawFromBagToCloudThreePlayersAction extends DrawFromBagToCloudAction {
    public DrawFromBagToCloudThreePlayersAction(GameEngine gameEngine) {
        super(gameEngine);
    }

    @Override
    void fromBagToCloud() throws EmptyBagException, TableNotSetException {
        for (int i = 1; i <= ModelConstants.THREE_PLAYERS; i++) {
            this.getGameEngine().getSchoolPawnManager().moveStudentsFromBagToCloud(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_THREE_PLAYERS_STUDENTS_NUMBER, i);
        }
    }

}