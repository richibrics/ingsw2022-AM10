package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class OnSelectionOfWizardAction extends Action{

    public OnSelectionOfWizardAction(GameEngine gameEngine) {
        super(0, gameEngine);
    }

    @Override
    public void setPlayerId(int playerId) {

    }

    @Override
    public void setOptions(String options) {

    }

    public void act() throws Exception {
        int chosenWizardId = 1; // This will be taken from the message options
        this.getGameEngine().getAssistantManager().setWizard(this.getPlayerId(), chosenWizardId);
    }

    @Override
    void modifyRound() {

    }

}
