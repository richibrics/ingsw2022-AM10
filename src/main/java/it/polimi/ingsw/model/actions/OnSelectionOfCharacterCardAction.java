package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.CharacterCard;

import java.util.HashMap;
import java.util.Map;

public class OnSelectionOfCharacterCardAction extends Action {
    public OnSelectionOfCharacterCardAction(GameEngine gameEngine) {
        super(3, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
    }
}
