package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that manages the action of JesterEffect.
 */
public class JesterEffectAction extends Action {

    private Integer studentId1;
    private Integer studentId2 = null;
    private Integer studentId3 = null;
    private Integer studentInStorageId1;
    private Integer studentInStorageId2 = null;
    private Integer studentInStorageId3 = null;

    public JesterEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_JESTER_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get from one to three different student Ids in the entrance
     * and the other Ids in the card storage from the options.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the entrance");
        try {
            this.studentId1 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
        }
        if (!options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id in the storage");
        try {
            this.studentInStorageId1 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id in the storage from the ActionMessage");
        }

        this.studentId2 = null;
        this.studentInStorageId2 = null;
        if (options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2)) {
            try {
                this.studentId2 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
            }
        }
        if (options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2)) {
            try {
                this.studentInStorageId2 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the storage from the ActionMessage");
            }
        }
        if ((studentId2 == null && studentInStorageId2 != null) || (studentId2 != null && studentInStorageId2 == null))
            throw new WrongMessageContentException("studentId2 or studentInStorage2 is null");

        this.studentId3 = null;
        this.studentInStorageId3 = null;
        if (options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3)) {
            try {
                this.studentId3 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the entrance from the ActionMessage");
            }
        }
        if (options.containsKey(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3)) {
            try {
                this.studentInStorageId3 = Integer.parseInt(options.get(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3));
            } catch (NumberFormatException e) {
                throw new WrongMessageContentException("Error while parsing student id in the storage from the ActionMessage");
            }
        }
        if ((studentId3 == null && studentInStorageId3 != null) || (studentId3 != null && studentInStorageId3 == null))
            throw new WrongMessageContentException("studentId3 or studentInStorage3 is null");


    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    /**
     * Switches from one to three entrance students specified in options
     * with the card storage students specified in options.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        CharacterCard characterCard = this.getGameEngine().getTable().getCharacterCards().get(Character.JESTER.getId());
        StudentDisc studentInEntrance1 = null;
        StudentDisc studentInEntrance2 = null;
        StudentDisc studentInEntrance3 = null;
        StudentDisc studentInStorage1 = null;
        StudentDisc studentInStorage2 = null;
        StudentDisc studentInStorage3 = null;
        for (StudentDisc studentDisc : characterCard.getStudentsStorage()) {
            if (studentDisc.getId() == this.studentInStorageId1)
                studentInStorage1 = studentDisc;
            if (studentInStorageId2 != null && studentDisc.getId() == this.studentInStorageId2)
                studentInStorage2 = studentDisc;
            if (studentInStorageId3 != null && studentDisc.getId() == this.studentInStorageId3)
                studentInStorage3 = studentDisc;
        }
        if (studentInStorage1 == null || (studentInStorage2 == null && studentInStorageId2 != null) || (studentInStorage3 == null && studentInStorageId3 != null)) {
            throw new IllegalGameActionException("The student requested isn't in card storage");
        }
        for (StudentDisc studentDisc : CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).getEntrance()) {
            if (studentDisc.getId() == this.studentId1)
                studentInEntrance1 = studentDisc;
            if (this.studentId2 != null && studentDisc.getId() == this.studentId2)
                studentInEntrance2 = studentDisc;
            if (this.studentId3 != null && studentDisc.getId() == this.studentId3)
                studentInEntrance3 = studentDisc;
        }
        if (studentInEntrance1 == null || (studentInEntrance2 == null && this.studentId2 != null) || (studentInEntrance3 == null && this.studentId3 != null)) {
            throw new IllegalGameActionException("The student requested isn't in the entrance");
        }
        characterCard.removeStudentFromStorage(studentInStorageId1);
        if (studentInStorageId2 != null) {
            characterCard.removeStudentFromStorage(studentInStorageId2);
        }
        if (studentInStorageId3 != null) {
            characterCard.removeStudentFromStorage(studentInStorageId3);
        }
        ArrayList<StudentDisc> studentsToEntrance = new ArrayList<>();
        studentsToEntrance.add(studentInStorage1);
        if (studentInStorageId2 != null) {
            studentsToEntrance.add(studentInStorage2);
        }
        if (studentInStorageId3 != null) {
            studentsToEntrance.add(studentInStorage3);
        }
        CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).removeStudentFromEntrance(studentId1);
        if (studentId2 != null) {
            CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).removeStudentFromEntrance(studentId2);
        }
        if (studentId3 != null) {
            CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).removeStudentFromEntrance(studentId3);
        }
        characterCard.addStudentToStorage(studentInEntrance1);
        if (studentId2 != null) {
            characterCard.addStudentToStorage(studentInEntrance2);
        }
        if (studentId3 != null) {
            characterCard.addStudentToStorage(studentInEntrance3);
        }
        CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).addStudentsToEntrance(studentsToEntrance);
    }
}
