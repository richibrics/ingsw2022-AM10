package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.*;

import java.util.Arrays;

public class SetUpTwoAndFourPlayersAction extends SetUp {

    public SetUpTwoAndFourPlayersAction(GameEngine gameEngine) {
        super(gameEngine);
    }

    protected void setUpTowers() {
        int index = 0;
        for (Team team : this.getGameEngine().getTeams()) {
            for (int i = ModelConstants.MIN_ID_OF_TOWER; i <= ModelConstants.NUMBER_OF_TOWERS_TWO_FOUR_PLAYERS; i++)
                team.addTower(new Tower(TowerColor.values()[index]));
            index++;
        }
    }

    protected void drawStudentsAndPlaceOnEntrance(Bag bag) throws SchoolBoardNotSetException, EmptyBagException {
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                player.getSchoolBoard().addStudentsToEntrance(bag.drawStudents(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_TWO_FOUR_PLAYERS));
    }
}