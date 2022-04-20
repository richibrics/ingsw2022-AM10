package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Tower;
import it.polimi.ingsw.model.game_components.TowerColor;

public class SetUpThreePlayersAction extends SetUp {

    public SetUpThreePlayersAction(GameEngine gameEngine) {
        super(gameEngine);
    }

    protected void setUpTowers() {
        int index = 0;
        for (TowerColor color : TowerColor.values()) {
            for (int i = 1; i <= 6; i++)
                this.getGameEngine().getTeams().get(index).addTower(new Tower(color));
            index++;
        }
    }

    protected void drawStudentsAndPlaceOnEntrance(Bag bag) throws SchoolBoardNotSetException, EmptyBagException {
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                player.getSchoolBoard().addStudentsToEntrance(bag.drawStudents(9));
    }
}