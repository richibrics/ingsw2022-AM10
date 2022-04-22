package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.ArrayList;
import java.util.Map;

public class MoveStudentsFromEntranceAction extends Action {

    private int countMovedStudents;

    public MoveStudentsFromEntranceAction(GameEngine gameEngine) {
        super(4, gameEngine);
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    @Override
    public void act() throws Exception {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        this.moveStudentFromEntranceToDiningRoom();
        this.moveStudentFromEntranceToIsland();
    }

    protected void moveStudentFromEntranceToDiningRoom() {
    }

    public void moveStudentFromEntranceToIsland() {
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }
}
