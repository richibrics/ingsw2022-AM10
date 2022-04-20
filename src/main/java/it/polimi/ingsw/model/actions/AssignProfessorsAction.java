package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.Map;

public class AssignProfessorsAction extends AbstractAssignProfessorAction {

    public AssignProfessorsAction (GameEngine gameEngine) { super(gameEngine); }

    /**
     * Implements the behaviour of the action.
     */
    @Override
    public void act() throws Exception {

    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     *
     * @throws Exception if something bad happens
     */
    @Override
    public void modifyRound() throws Exception {

    }
}