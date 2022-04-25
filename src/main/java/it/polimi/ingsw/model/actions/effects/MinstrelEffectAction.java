package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.ArrayList;
import java.util.Map;

public class MinstrelEffectAction extends Action {
    public MinstrelEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MINSTREL_ID, gameEngine);
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
        this.moveStudentFromDiningRoomToEntrance(studentDiscsToObtain, studentDiscsToRemove, schoolBoards);
    }

    private void moveStudentFromDiningRoomToEntrance(ArrayList<StudentDisc> studentDiscsToObtain, ArrayList<StudentDisc> studentDiscsToRemove, ArrayList<SchoolBoard> schoolBoards) throws Exception {
        CharacterCard characterCard = new CharacterCard(Character.MINSTREL);
        ArrayList<StudentDisc> studentDiscsSwitched = new ArrayList<>();
        if (getGameEngine().getNumberOfPlayers() != 3) {
            for (int i = 0; i < 7; i++) {
                if (studentDiscsToObtain.get(0).getId() == schoolBoards.get(getId()).removeStudentFromEntrance(i).getId()) {
                    schoolBoards.get(getId()).removeStudentFromEntrance(i);
                    studentDiscsSwitched.add(studentDiscsToObtain.get(0));
                    if (studentDiscsSwitched.size() > 4) {
                        throw new IllegalStudentDiscMovementException("You can only switch 2 or less studentDisc");
                    }
                }
            }
            for (int x = 0; x < 4; x++) {
                for (int j = 0; j < 11; j++) {
                    if (studentDiscsToRemove.get(0).getId() == schoolBoards.get(0).getDiningRoom().get(x).get(j).getId()) {
                    }
                }
            }
        }
    }
}
