package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Wizard;

import java.util.Map;

public abstract class OnSelectionOfAssistantsCardAction extends Action{
    public OnSelectionOfAssistantsCardAction(GameEngine gameEngine) {
        super(2, gameEngine);
    }


    public void setOptions(Map<String, String> options) throws Exception{

    }

    @Override
    public void act() throws Exception {

    }
}
