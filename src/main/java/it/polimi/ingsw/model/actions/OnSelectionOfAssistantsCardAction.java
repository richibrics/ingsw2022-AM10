package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Wizard;

public abstract class OnSelectionOfAssistantsCardAction extends Action{
    public OnSelectionOfWizardAction(GameEngine gameEngine) {
        super(2, gameEngine);
    }

    public void setPlayerId(int playerId) {

    }

    public void setOptions(String options) {

    }

    @Override
    public void act() throws Exception {

    }
}
