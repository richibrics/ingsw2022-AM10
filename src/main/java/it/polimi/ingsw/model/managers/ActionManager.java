package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.*;

public class ActionManager extends Manager {

    private Action[] actions = new Action[10];
    //TODO observers

    public ActionManager(GameEngine gameEngine) { super(gameEngine); }

    /**
     * Generates the set-up and all the standard actions, which are the actions with id between 0 and 9.
     * @throws Exception if an operation throws an exception
     */

    public void generateActions() throws Exception
    {
        this.generateSetUp();
        this.generateArrayOfActions();
        //notifyAllObservers();
    }

    /**
     * Generates and executes a SetUp action.
     * @throws Exception if the SetUp action throws an exception
     */

    private void generateSetUp() throws Exception
    {
        SetUp setUp = null;
        //The method checks the number of players and creates a setUpAction accordingly.
        if (this.getGameEngine().getNumberOfPlayers() == 2 || this.getGameEngine().getNumberOfPlayers() == 4)
            setUp = new SetUpTwoAndFourPlayersAction(this.getGameEngine());
        else if (this.getGameEngine().getNumberOfPlayers() == 3)
            setUp = new SetUpThreePlayersAction(this.getGameEngine());

        setUp.act();
    }

    /**
     * Generates all the standard actions.
     */

    //TODO
    private void generateArrayOfActions()
    {

    }

    /**
     * Sets the playerId and the options of the action with {@code actionId}, then executes the action.
     * @param actionId the identifier of the action
     * @param playerId the identifier of the player performing the action
     * @param options the string of options
     * @throws Exception if the action executed throws an exception
     */

    public void executeAction(int actionId, int playerId, String options) throws Exception
    {
        actions[actionId].setPlayerId(playerId);
        actions[actionId].setOptions(options);
        actions[actionId].act();
    }

    /**
     * Notifies all the observers attached to the ActionManager.
     */

    //TODO
    public void notifyAllObservers()
    {

    }
}