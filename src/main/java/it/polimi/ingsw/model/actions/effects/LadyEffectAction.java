package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Character;

import java.util.ArrayList;
import java.util.Map;

public class LadyEffectAction extends Action {
    public LadyEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_LADY_ID, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

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
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        this.moveStudentFromCardToDiningRoom(studentDiscs, schoolBoards);
    }

    private void moveStudentFromCardToDiningRoom(ArrayList<StudentDisc> studentDiscs, ArrayList<SchoolBoard> schoolBoards) throws Exception {
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentToSwitch = new ArrayList<>();
        CharacterCard characterCard = new CharacterCard(Character.LADY);
        for (int i = 0; i < 4; i++) {
            if (studentDiscs.get(0).getId() == characterCard.removeStudentFromStorage(i).getId()) {
                characterCard.removeStudentFromStorage(i);
                StudentDisc student = characterCard.removeStudentFromStorage(i);
                studentToSwitch.add(student);
                if (studentToSwitch.size() > 1) {
                    throw new IllegalStudentDiscMovementException("You can only move one StudentDisc");
                } else schoolBoards.get(getId()).addStudentToDiningRoom(student);
            }
        }
        this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, bag);
    }

}
