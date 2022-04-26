package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.AbstractAssignProfessorAction;
import it.polimi.ingsw.model.actions.Action;

public abstract class AssignProfessorActionEffect extends AbstractAssignProfessorAction {

    private final Action assignProfessorAction;

    public AssignProfessorActionEffect(GameEngine gameEngine, Action assignProfessorAction) {
        super(gameEngine);
        this.assignProfessorAction = assignProfessorAction;
    }

    /**
     * Gets the assign professor action.
     *
     * @return the assign professor action
     */

    public Action getAssignProfessorAction() {
        return this.assignProfessorAction;
    }

    /**
     * Implements the behaviour of the action.
     */

    @Override
    public void act() throws Exception {
        super.act();
    }

    /**
     * Modifies the list of standard actions in the action manager.
     */

    private void modifyActionList() {
        this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_ASSIGN_PROFESSORS_ID] = this.getAssignProfessorAction();
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        this.modifyActionList();
    }
}
