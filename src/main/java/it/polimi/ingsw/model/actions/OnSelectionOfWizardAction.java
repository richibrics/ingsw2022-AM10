package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Bag;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class OnSelectionOfWizardAction extends Action{

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
        Bag bag = new Bag();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        }

    @Override
    void modifyRound() {

    }

}
