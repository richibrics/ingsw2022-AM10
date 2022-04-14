package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.*;

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
        Bag bag = new Bag();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        ArrayList<IslandTile> islandTiles = new ArrayList<>();
        this.getGameEngine().getSchoolPawnManager();
        this.fromBagToCloud(bag);
    }

    void fromBagToCloud(Bag bag) throws EmptyBagException, TableNotSetException {
    }

}
