package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

public abstract class AssignProfessorsAction extends Action {
    public AssignProfessorsAction (GameEngine gameEngine) { super(8, gameEngine); }
}
