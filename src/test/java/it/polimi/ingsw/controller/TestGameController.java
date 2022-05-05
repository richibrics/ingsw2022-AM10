package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestGameController {

    /**
     * Tests getGameEngine, tests teams are created and setup run.
     * Test for 2 players.
     */
    @Test
    void startGame2Players() {
        HashMap<User, ServerClientConnection> connections = new HashMap<>();
        connections.put(new User("1", 2), null);
        connections.put(new User("2", 2), null);
        GameController gameController = new GameController(connections);
        assertNull(gameController.getGameEngine());
        assertDoesNotThrow(gameController::startGame);
        assertNotNull(gameController.getGameEngine());
        assertEquals(2, gameController.getGameEngine().getNumberOfPlayers());
        assertEquals(2, gameController.getGameEngine().getTeams().size());
        assertEquals(2, assertDoesNotThrow(()->gameController.getGameEngine().getTable().getCloudTiles().size()));
    }

    /**
     * Tests getGameEngine, tests teams are created and setup run.
     * Test for 3 players.
     */
    @Test
    void startGame3Players() {
        HashMap<User, ServerClientConnection> connections = new HashMap<>();
        connections.put(new User("1", 3), null);
        connections.put(new User("2", 3), null);
        connections.put(new User("3", 3), null);
        GameController gameController = new GameController(connections);
        assertNull(gameController.getGameEngine());
        assertDoesNotThrow(gameController::startGame);
        assertNotNull(gameController.getGameEngine());
        assertEquals(3, gameController.getGameEngine().getNumberOfPlayers());
        assertEquals(3, gameController.getGameEngine().getTeams().size());
        assertEquals(3, assertDoesNotThrow(()->gameController.getGameEngine().getTable().getCloudTiles().size()));
    }

    /**
     * Tests getGameEngine, tests teams are created and setup run.
     * Test for 4 players.
     */
    @Test
    void startGame4Players() {
        HashMap<User, ServerClientConnection> connections = new HashMap<>();
        connections.put(new User("1", 4), null);
        connections.put(new User("2", 4), null);
        connections.put(new User("3", 4), null);
        connections.put(new User("4", 4), null);
        GameController gameController = new GameController(connections);
        assertNull(gameController.getGameEngine());
        assertDoesNotThrow(gameController::startGame);
        assertNotNull(gameController.getGameEngine());
        assertEquals(4, gameController.getGameEngine().getNumberOfPlayers());
        assertEquals(2, gameController.getGameEngine().getTeams().size());
        assertEquals(4, assertDoesNotThrow(()->gameController.getGameEngine().getTable().getCloudTiles().size()));
    }

    /**
     * Tests an action is run, in this example I select a wizard, at first with a player that doesn't have the turn (check no edits made),
     * then with the correct one (check edits).
     * The with correct player try to run an action that is not in actions list in Round (check no edits made).
     */
    @Test
    void resumeGame() {
        HashMap<User, ServerClientConnection> connections = new HashMap<>();
        connections.put(new User("1", 2), null);
        connections.put(new User("2", 2), null);
        GameController gameController = new GameController(connections);
        assertDoesNotThrow(gameController::startGame);

        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD, "2");
        ActionMessage wizardSelect = new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, options);

        assertTrue(gameController.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID));
        int playerWithTurn = assertDoesNotThrow(()->gameController.getGameEngine().getRound().getCurrentPlayer());
        int playerNotWithTurn = (assertDoesNotThrow(()->gameController.getGameEngine().getRound().getCurrentPlayer())%2)+1;

        // Ask for action to player that has not the turn and check that he hasn't selected the wizard
        ActionMessage finalWizardSelect = wizardSelect;
        assertThrows(IllegalGameActionException.class, ()->gameController.resumeGame(playerNotWithTurn, finalWizardSelect));
        assertFalse(assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(), playerNotWithTurn).hasWizard()));

        // Now the command is from player who has rights: check wizard selected
        int finalPlayerWithTurn1 = playerWithTurn;
        ActionMessage finalWizardSelect1 = wizardSelect;
        assertDoesNotThrow(()->gameController.resumeGame(finalPlayerWithTurn1, finalWizardSelect1));
        int finalPlayerWithTurn = playerWithTurn;
        assertTrue(assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(), finalPlayerWithTurn).hasWizard()));

        // Now the round should have gone forward and the ex-player who didn't have the turn, now has it
        options.clear();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD, "1");
        wizardSelect = new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, options);
        ActionMessage finalWizardSelect2 = wizardSelect;
        assertDoesNotThrow(()->gameController.resumeGame(playerNotWithTurn, finalWizardSelect2));
        assertTrue(assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(), playerNotWithTurn).hasWizard()));

        // Last check: player can't select Action not in round: play Herbalist card to set NoEntryTile and check it hasn't been set
        playerWithTurn = assertDoesNotThrow(()->gameController.getGameEngine().getRound().getCurrentPlayer());
        options.clear();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.HERBALIST.getId()));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "3");
        ActionMessage playCardHerbalist = new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID, options);
        int finalPlayerWithTurn2 = playerWithTurn;
        assertThrows(IllegalGameActionException.class, ()->gameController.resumeGame(finalPlayerWithTurn2,playCardHerbalist));
        assertFalse(assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameController.getGameEngine(),3).hasNoEntry()));
    }

    @Test
    void interruptGame() {

    }
}