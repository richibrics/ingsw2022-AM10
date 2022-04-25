package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.ArrayList;
import java.util.Map;

public class JesterEffectAction extends Action {
    public JesterEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_JESTER_ID, gameEngine);
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
        ArrayList<StudentDisc> studentDiscsToObtain = new ArrayList<>();
        ArrayList<StudentDisc> studentDiscsToRemove = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        this.moveStudentFromCardToEntrance(studentDiscsToObtain, studentDiscsToRemove, schoolBoards);
    }

    private void moveStudentFromCardToEntrance(ArrayList<StudentDisc> studentDiscsToObtain, ArrayList<StudentDisc> studentDiscsToRemove, ArrayList<SchoolBoard> schoolBoards) throws Exception {
        CharacterCard characterCard = new CharacterCard(Character.JESTER);
        ArrayList<StudentDisc> studentDiscsSwitched = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (studentDiscsToObtain.get(0).getId() == characterCard.removeStudentFromStorage(i).getId()) {
                characterCard.removeStudentFromStorage(i);
                if (getGameEngine().getNumberOfPlayers() != 3) {
                    for (int j = 0; j < 7; j++) {
                        if (studentDiscsToRemove.get(0).getId() == schoolBoards.get(getId()).removeStudentFromEntrance(j).getId()) {
                            schoolBoards.get(getId()).removeStudentFromEntrance(j);
                            studentDiscsSwitched.add(studentDiscsToRemove.get(0));
                            if (studentDiscsSwitched.size() > 4) {
                                throw new IllegalStudentDiscMovementException("You can only switch 3 or less studentDisc");
                            }
                        }
                    }
                } else for (int j = 0; j < 9; j++) {
                    if (studentDiscsToRemove.get(0).getId() == schoolBoards.get(getId()).removeStudentFromEntrance(j).getId()) {
                        schoolBoards.get(getId()).removeStudentFromEntrance(j);
                        studentDiscsSwitched.add(studentDiscsToRemove.get(0));
                        if (studentDiscsSwitched.size() > 4) {
                            throw new IllegalStudentDiscMovementException("You can only switch 3 or less studentDisc");
                        }
                    }
                }
            }
        }
        for (int y = 0; y < 4; y++) {
            characterCard.addStudentToStorage(studentDiscsSwitched.get(y));
            schoolBoards.get(getId()).addStudentsToEntrance(studentDiscsToObtain);
        }
    }


}
