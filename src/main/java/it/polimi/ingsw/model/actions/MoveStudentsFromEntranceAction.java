package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import javax.lang.model.element.ModuleElement;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

public class MoveStudentsFromEntranceAction extends Action {
    private Integer studentToMove;
    private Integer futureStudentPosition;
    private int countMovedStudents;

    public MoveStudentsFromEntranceAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID, gameEngine);
        this.countMovedStudents = 0;
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * Here I parse the id of the student and its next position.
     *
     * @param options additional information for act method
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        // Parse the student to move
        if (!options.containsKey(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id");
        try {
            this.studentToMove = Integer.parseInt(options.get(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id from the ActionMessage");
        }

        // Parse the position where the student will be placed and check it's valid
        if (!options.containsKey(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION))
            throw new WrongMessageContentException("ActionMessage doesn't contain the future student position");
        try {
            this.futureStudentPosition = Integer.parseInt(options.get(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing the future student position from the ActionMessage");
        }
        if (this.futureStudentPosition != ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION_VALUE_DINING_ROOM
                && (this.futureStudentPosition < ModelConstants.MIN_ID_OF_ISLAND || this.futureStudentPosition > ModelConstants.NUMBER_OF_ISLAND_TILES))
            throw new WrongMessageContentException("Future student position is not valid");
    }

    /**
     * Adds the requested student to the dining room or to the island tile.
     * The student and the position are defined in the options.
     * @throws Exception if something bad happens
     */
    @Override
    public void act() throws Exception {
        try {
            if (this.futureStudentPosition == ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION_VALUE_DINING_ROOM) {
                // Student goes to the dining room
                this.getGameEngine().getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(this.getPlayerId(), this.studentToMove);
            } else {
                // Student goes to the island tile (which id is in this.futureStudentPosition)
                this.getGameEngine().getSchoolPawnManager().moveStudentFromEntranceToIsland(this.getPlayerId(), this.studentToMove, this.futureStudentPosition);
            }
        } catch (SchoolBoardNotSetException | TableNotSetException e) {
            throw new IllegalGameStateException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new IllegalGameActionException(e.getMessage());
        }
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case I count the number of times the player has moved a student. If it's 3 I can remove this action
     * from the playable actions and add the next ones.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        this.countMovedStudents += 1;
        if(this.countMovedStudents >= ModelConstants.NUMBER_OF_MOVEMENTS_FROM_ENTRANCE) {
            this.countMovedStudents = 0;
            ArrayList<Integer> nextActions = this.getGameEngine().getRound().getPossibleActions();
            // Remove this action from round
            nextActions.remove(Integer.valueOf(this.getId()));
            // Add next actions
            nextActions.add(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID);
            // Set actions
            this.getGameEngine().getRound().setPossibleActions(nextActions);
        }
        // Otherwise, round is not edited
    }
}
