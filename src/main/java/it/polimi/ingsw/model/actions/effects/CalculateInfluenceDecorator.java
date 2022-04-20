package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.AbstractCalculateInfluenceAction;

public abstract class CalculateInfluenceDecorator extends AbstractCalculateInfluenceAction {

    private AbstractCalculateInfluenceAction calculateInfluenceAction;

    public CalculateInfluenceDecorator(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Sets the instance of type {@code AbstractCalculateInfluenceAction}.
     *
     * @param calculateInfluenceAction the instance of the class {@code AbstractCalculateInfluenceAction}
     */

    public void setCalculateInfluenceAction(AbstractCalculateInfluenceAction calculateInfluenceAction) {
        this.calculateInfluenceAction = calculateInfluenceAction;
    }

    /**
     * Gets the instance of type {@code AbstractCalculateInfluenceAction}.
     *
     * @return the instance of the class {@code AbstractCalculateInfluenceAction}
     */

    public AbstractCalculateInfluenceAction getCalculateInfluenceAction() {
        return this.calculateInfluenceAction;
    }
}
