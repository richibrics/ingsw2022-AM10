package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

public class LadyEffectAction extends Action {
    private Integer studentToMove;

    public LadyEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_LADY_ID, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id");
        try {
            this.studentToMove = Integer.parseInt(options.get(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id from the ActionMessage");
        }
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
        CharacterCard characterCard = this.getGameEngine().getTable().getCharacterCards().get(Character.LADY.getId());
        StudentDisc studentInStorage = null;
        for (StudentDisc studentDisc : characterCard.getStudentsStorage()) {
            if (studentDisc.getId() == this.studentToMove) {
                studentInStorage = studentDisc;
            }
        }
        if (studentInStorage == null) {
            throw new IllegalGameActionException("The student requested isn't in card storage");
        }
        characterCard.removeStudentFromStorage(this.studentToMove);
        CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), getPlayerId()).addStudentToDiningRoom(studentInStorage);
        try {
            this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, getGameEngine().getTable().getBag());
        } catch (EmptyBagException e) {
            // the bag is empty, but the game can go on until the end of the round
        }
    }
}
