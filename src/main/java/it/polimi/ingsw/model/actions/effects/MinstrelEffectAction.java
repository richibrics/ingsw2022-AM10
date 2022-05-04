package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.Map;

public class MinstrelEffectAction extends Action {

    private Integer studentInEntrance1Id;
    private Integer studentInEntrance2Id = null;
    private Integer studentInDiningRoom1Id;
    private Integer studentInDiningRoom2Id = null;


    public MinstrelEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MINSTREL_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get from one to two different student Ids in the DiningRoom
     * and the other student Ids in the entrance.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the diningRoom");
        try {
            this.studentInDiningRoom1Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the diningRoom from the ActionMessage");
        }
        if (!options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the entrance");
        try {
            this.studentInEntrance1Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
        }

        this.studentInDiningRoom2Id = null;
        this.studentInEntrance2Id = null;
        if (options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2)) {
            try {
                this.studentInDiningRoom2Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the diningRoom from the ActionMessage");
            }
        }
        if (options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2)) {
            try {
                this.studentInEntrance2Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
            }
        }
        if ((studentInEntrance2Id == null && studentInDiningRoom2Id != null) || (studentInEntrance2Id != null && studentInDiningRoom2Id == null))
            throw new WrongMessageContentException("studentInEntrance2Id or studentInDiningRoom2Id is null");

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens.
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    /**
     * Switches from one to two different students of the entrance with other students of the DiningRoom.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        SchoolBoard schoolBoard = CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId());
        StudentDisc studentInEntrance1 = null;
        StudentDisc studentInEntrance2 = null;
        StudentDisc studentInDiningRoom1 = null;
        StudentDisc studentInDiningRoom2 = null;
        for (PawnColor color : PawnColor.values()) {
            for (StudentDisc studentDisc : schoolBoard.getDiningRoomColor(color)) {
                if (studentDisc.getId() == this.studentInDiningRoom1Id)
                    studentInDiningRoom1 = studentDisc;
                if (studentInDiningRoom2Id != null && studentDisc.getId() == this.studentInDiningRoom2Id)
                    studentInDiningRoom2 = studentDisc;
            }
        }
        for (StudentDisc studentDisc : schoolBoard.getEntrance()) {
            if (studentDisc.getId() == this.studentInEntrance1Id)
                studentInEntrance1 = studentDisc;
            if (studentInEntrance2Id != null && studentDisc.getId() == this.studentInEntrance2Id)
                studentInEntrance2 = studentDisc;
        }
        if (studentInEntrance1 == null || (studentInEntrance2Id != null && studentInEntrance2 == null)) {
            throw new IllegalGameActionException("The student requested isn't in the entrance");
        }
        if (studentInDiningRoom1 == null || (studentInDiningRoom2Id != null && studentInDiningRoom2 == null)) {
            throw new IllegalGameActionException("The student requested isn't in the diningRoom");
        }
        if (studentInDiningRoom2Id != null) {
            // If I have to move 2 students from the dining room, and if these students are in
            // the same table, then I have to move the student in the last position of the table first
            // and then the other one. So now I check which one is the last one, and I move it first
            if (studentInDiningRoom1.getColor() == studentInDiningRoom2.getColor()) {
                // Check which one is the last on the table
                if (schoolBoard.getDiningRoomColor(studentInDiningRoom1.getColor()).get(schoolBoard.getDiningRoomColor(studentInDiningRoom1.getColor()).size() - 1) == studentInDiningRoom1) {
                    // studentInDiningRoom1 is the last. Move 1, then 2
                    schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom1);
                    schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom2);
                    schoolBoard.addStudentToDiningRoom(studentInEntrance1);
                    schoolBoard.addStudentToDiningRoom(studentInEntrance2);

                } else if (schoolBoard.getDiningRoomColor(studentInDiningRoom2.getColor()).get(schoolBoard.getDiningRoomColor(studentInDiningRoom2.getColor()).size() - 1) == studentInDiningRoom2) {
                    // studentInDiningRoom2 is the last. Move 2, then 1
                    schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom2);
                    schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom1);
                    schoolBoard.addStudentToDiningRoom(studentInEntrance1);
                    schoolBoard.addStudentToDiningRoom(studentInEntrance2);
                } else {
                    // Nobody is the last: exception
                    throw new IllegalStudentDiscMovementException("The two studentInDiningRoom aren't in the last position");
                }
            } else {
                schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom1);
                schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom2);
                schoolBoard.addStudentToDiningRoom(studentInEntrance1);
                schoolBoard.addStudentToDiningRoom(studentInEntrance2);
            }
        } else { // I have to move only a student, and I have no problems
            schoolBoard.removeStudentFromDiningRoom(studentInDiningRoom1);
            schoolBoard.addStudentToDiningRoom(studentInEntrance1);
        }
        ArrayList<StudentDisc> studentsToEntrance = new ArrayList<>();
        studentsToEntrance.add(studentInDiningRoom1);
        if (studentInDiningRoom2Id != null) {
            studentsToEntrance.add(studentInDiningRoom2);
        }
        schoolBoard.addStudentsToEntrance(studentsToEntrance);
    }

}
