package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.*;

import java.util.Arrays;

public class SetUpTwoAndFourPlayersAction extends SetUp {

    public SetUpTwoAndFourPlayersAction(GameEngine gameEngine) { super(gameEngine); }

    protected void setUpTowers()
    {
        int index = 0;
        for (Team team : this.getGameEngine().getTeams()) {
            for (int i = 1; i <= 8; i++)
                team.addTower(new Tower(TowerColor.values()[index]));
            index++;
        }
    }

    protected void drawStudentsAndPlaceOnEntrance(Bag bag) throws SchoolBoardNotSetException, EmptyBagException
    {
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                player.getSchoolBoard().addStudentsToEntrance(bag.drawStudents(7));
    }
}