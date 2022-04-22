package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

import java.util.Map;

public abstract class Action {

    private final int id;
    private int playerId;
    private GameEngine gameEngine;

    public Action(int id, GameEngine gameEngine) {
        this.id = id;
        this.gameEngine = gameEngine;
    }

    /**
     * Implements the behaviour of the action.
     */

    public abstract void act() throws Exception;

    /**
     * Returns the identifier of the Action class.
     *
     * @return the id of the Action class.
     */

    public int getId() {
        return this.id;
    }

    /**
     * Gets the playerId of the player performing the action.
     *
     * @return the playerId of the player performing the action
     */

    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Sets the playerId, which indicates the player performing the action.
     *
     * @param playerId the id of the player performing the action
     */

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method
     */
    public abstract void setOptions(Map<String, String> options) throws Exception;

    /**
     * Gets the game engine.
     *
     * @return the game engine
     */

    public GameEngine getGameEngine() {
        return this.gameEngine;
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    public abstract void modifyRoundAndActionList() throws Exception;
}