package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;

import java.util.Map;

public interface ActionDecorator {
    /**
     * Implements the behaviour of the action.
     */

    void act() throws Exception;

    /**
     * Returns the identifier of the Action class.
     *
     * @return the id of the Action class.
     */

    int getId();

    /**
     * Gets the playerId of the player performing the action.
     *
     * @return the playerId of the player performing the action
     */

    int getPlayerId();

    /**
     * Sets the playerId, which indicates the player performing the action.
     *
     * @param playerId the id of the player performing the action
     */

    void setPlayerId(int playerId);

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method
     */
    void setOptions(Map<String, String> options) throws Exception;

    /**
     * Gets the game engine.
     *
     * @return the game engine
     */

    GameEngine getGameEngine();

    /**
     * Sets the instance of Decorator type of this action
     * @param actionToDecorate the instance of the class {@code abstractAction}
     */
    void setActionToDecorate(Action actionToDecorate);

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     * @throws Exception if something bad happens
     */
    void modifyRound() throws Exception;
}
