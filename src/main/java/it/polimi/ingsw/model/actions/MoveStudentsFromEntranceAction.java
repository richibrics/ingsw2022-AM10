package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.ArrayList;

public abstract class MoveStudentsFromEntranceAction extends Action{

    private int countMovedStudents;

    public MoveStudentsFromEntranceAction(GameEngine gameEngine) {
        super(4,gameEngine);
    }

    @Override
    public void setPlayerId(int playerId) {

    }

    @Override
    public void setOptions(String options) {

    }

    @Override
    public void act() throws Exception {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        this.moveStudentFromEntranceToDiningRoom();
        this.moveStudentFromEntranceToIsland();
    }

    protected void moveStudentFromEntranceToDiningRoom(){
    }

    public void moveStudentFromEntranceToIsland(){
    }

    @Override
    void modifyRound() {

    }
}