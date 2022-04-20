package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.CharacterCard;

import java.util.HashMap;
import java.util.Map;

public abstract class OnSelectionOfCharacterCardAction extends Action{
    public OnSelectionOfCharacterCardAction(GameEngine gameEngine) {
        super(3,gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception{

    }

    @Override
    public void act() throws Exception {
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
    }
}
