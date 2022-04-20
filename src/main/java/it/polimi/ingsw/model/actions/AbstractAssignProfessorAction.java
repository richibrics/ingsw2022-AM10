package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;

public abstract class AbstractAssignProfessorAction extends Action{

    public AbstractAssignProfessorAction (GameEngine gameEngine) { super(ModelConstants.ACTION_ASSIGN_PROFESSORS_ID, gameEngine); }
}
