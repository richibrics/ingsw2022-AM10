package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;

public abstract class Action {

    private final int id;
    private int playerId;
    private String options;
    private GameEngine gameEngine;

    public Action(int id, GameEngine gameEngine)
    {
        this.id = id;
        this.gameEngine = gameEngine;
    }

    /**
     * Implements the behaviour of the action.
     */

    public abstract void act() throws Exception;

    /**
     * Returns the identifier of the Action class.
     * @return the id of the Action class.
     */

    public int getId() {
        return this.id;
    }

    /**
     * Gets the playerId of the player performing the action.
     * @return the playerId of the player performing the action
     */

    public int getPlayerId() { return this.playerId; }

    /**
     * Sets the playerId, which indicates the player performing the action.
     * @param playerId the id of the player performing the action
     */

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the options.
     * @return a string of options
     */

    public String getOptions() { return this.options; }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * @param options additional information for act method
     */
    public abstract void setOptions(String options);

    /**
     * Gets the game engine.
     * @return the game engine
     */

    public GameEngine getGameEngine() { return this.gameEngine; }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     */

    abstract void modifyRound();
}