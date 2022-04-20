package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.*;

public class OnSelectionOfWizardAction extends Action {

    public OnSelectionOfWizardAction(GameEngine gameEngine) {
        super(0, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    public void act() throws Exception {
        int chosenWizardId = 1; // This will be taken from the message options
        this.getGameEngine().getAssistantManager().setWizard(this.getPlayerId(), chosenWizardId);
    }

    @Override
    public void modifyRound() throws Exception {

    }

}
