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

public class FriarEffectAction extends Action {
    public FriarEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_FRIAR_ID, gameEngine);
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
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        this.moveStudentFromCardToIsland(studentDiscs, islandGroups);
    }

    private void moveStudentFromCardToIsland(ArrayList<StudentDisc> studentDiscs, ArrayList<ArrayList<IslandTile>> islandGroups) throws Exception {
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentToObtain = new ArrayList<>();
        CharacterCard characterCard = new CharacterCard(Character.FRIAR);
        for (int i = 0; i < 4; i++) {
            if (studentDiscs.get(0).getId() == characterCard.removeStudentFromStorage(i).getId()) {
                characterCard.removeStudentFromStorage(i);
                StudentDisc student = characterCard.removeStudentFromStorage(i);
                studentToObtain.add(student);
                if (studentToObtain.size() > 1) {
                    throw new IllegalStudentDiscMovementException("You can only move one StudentDisc");
                } else for (int j = 0; i < 12; i++) {
                    IslandTile islandTile = new IslandTile(j);
                    if (islandGroups.get(getId()).get(0) == islandTile)
                        islandTile.addStudent(student);
                }
            }
        }
        this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, bag);
    }
}
