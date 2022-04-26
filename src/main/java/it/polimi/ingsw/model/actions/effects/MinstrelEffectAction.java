package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.PawnColor;
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

    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINDININGROOM1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the diningRoom");
        try {
            this.studentInDiningRoom1Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINDININGROOM1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the diningRoom from the ActionMessage");
        }
        if (!options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINENTRANCE1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the entrance");
        try {
            this.studentInEntrance1Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINENTRANCE1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
        }


        if (options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINDININGROOM2)) {
            try {
                this.studentInDiningRoom2Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINDININGROOM2));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the diningRoom from the ActionMessage");
            }
        }
        if (options.containsKey(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINENTRANCE2)) {
            try {
                this.studentInEntrance2Id = Integer.parseInt(options.get(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENTINENTRANCE2));
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
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {
        StudentDisc studentInEntrance1 = null;
        StudentDisc studentInEntrance2 = null;
        StudentDisc studentInDiningRoom1 = null;
        StudentDisc studentInDiningRoom2 = null;
        for (PawnColor color : PawnColor.values()) {
            for (StudentDisc studentDisc : CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).getDiningRoomColor(color)) {
                if (studentDisc.getId() == this.studentInDiningRoom1Id)
                    studentInDiningRoom1 = studentDisc;
                if (studentInDiningRoom2Id != null && studentDisc.getId() == this.studentInDiningRoom2Id)
                    studentInDiningRoom2 = studentDisc;
            }
        }
        for (StudentDisc studentDisc : CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).getEntrance()) {
            if (studentDisc.getId() == this.studentInEntrance1Id)
                studentInEntrance1 = studentDisc;
            if (studentInEntrance2Id != null && studentDisc.getId() == this.studentInEntrance2Id)
                studentInEntrance2 = studentDisc;
        }
        if (studentInEntrance1 == null || studentInEntrance2 == null) {
            throw new IllegalGameActionException("The student requested isn't in the entrance");
        }
        if (studentInDiningRoom1 == null || studentInDiningRoom2 == null) {
            throw new IllegalGameActionException("The student requested isn't in the diningRoom");
        }
        CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).replaceStudentInDiningRoom(studentInDiningRoom1, studentInEntrance1);
        if (studentInDiningRoom2Id != null) {
            CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).replaceStudentInDiningRoom(studentInDiningRoom2, studentInEntrance2);
        }
        ArrayList<StudentDisc> studentsToEntrance = new ArrayList<>();
        studentsToEntrance.add(studentInDiningRoom1);
        if (studentInDiningRoom2Id != null) {
            studentsToEntrance.add(studentInDiningRoom2);
        }
        CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).addStudentsToEntrance(studentsToEntrance);
    }
}
