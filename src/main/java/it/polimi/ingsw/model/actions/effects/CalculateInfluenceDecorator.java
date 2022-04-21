package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.AbstractCalculateInfluenceAction;
import it.polimi.ingsw.model.actions.Action;

public abstract class CalculateInfluenceDecorator extends AbstractCalculateInfluenceAction implements ActionDecorator {

    private AbstractCalculateInfluenceAction calculateInfluenceAction;

    public CalculateInfluenceDecorator(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Sets the instance of type {@code AbstractCalculateInfluenceAction}.
     * @param actionToDecorate the instance of the class {@code AbstractCalculateInfluenceAction}
     */

    public void setActionToDecorate(Action actionToDecorate) {
        this.calculateInfluenceAction = (AbstractCalculateInfluenceAction) actionToDecorate;
    }

    /**
     * Gets the instance of type {@code AbstractCalculateInfluenceAction}.
     *
     * @return the instance of the class {@code AbstractCalculateInfluenceAction}
     */

    public AbstractCalculateInfluenceAction getCalculateInfluenceAction() {
        return this.calculateInfluenceAction;
    }

    /**
     * Calculates the influence that each team has on the island group containing the mother nature pawn, checks if
     * changes are needed and makes the required changes to the island group.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void act() throws Exception {
        this.getCalculateInfluenceAction().act();
    }

    /**
     * Modifies the list of standard actions in the action manager.
     */

    private void modifyActionList() {
        this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID] = this.getCalculateInfluenceAction();
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRound() throws Exception {
        super.modifyRound();
        this.modifyActionList();
    }
}
