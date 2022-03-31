package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;

public abstract class Manager {

    private GameEngine gameEngine;

    public Manager(GameEngine gameEngine) { this.gameEngine = gameEngine; }

    public GameEngine getGameEngine() { return this.gameEngine; }
}
