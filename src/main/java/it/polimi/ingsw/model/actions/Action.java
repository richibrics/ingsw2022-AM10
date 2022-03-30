package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

public abstract class Action {

    int id;
    int playerId;
    String options;
    GameEngine gameEngine;

    /**
     * Implements the behaviour of the action.
     */

    public abstract void act();

    /**
     * Sets the playerId, which indicates the player performing the action.
     * @param playerId id of the player performing the action.
     */

    public abstract void setPlayerId(int playerId);

    /**
     * Sets the options. Options represents additional information used by the act method.
     * @param options additional information for act method.
     */
    public abstract void setOptions(String options);

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     */
    protected abstract void modifyRound();

    /**
     * Returns the identifier of the Action class.
     * @return the id of the Action class.
     */
    public int getId() {
        return this.id;
    }

}