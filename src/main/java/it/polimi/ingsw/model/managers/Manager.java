package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;

/**
 * Class that contains the method for the GameEngine.
 */
public abstract class Manager {

    private final GameEngine gameEngine;

    public Manager(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public GameEngine getGameEngine() {
        return this.gameEngine;
    }
}
