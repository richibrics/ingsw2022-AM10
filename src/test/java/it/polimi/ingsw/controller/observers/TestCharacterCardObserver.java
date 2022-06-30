package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestCharacterCardObserver {
    /**
     * Tests the observer send the correct messages to the correct ServerClientConnections when notifyClients is invoked.
     * This test doesn't invoke directly the observer but also checks the observer is invoked by the game controller
     * when the game starts.
     * Then checks that if the last played character card is not already set, the observer doesn't send anything, even if notified.
     * <p>
     * Use RepeatedTest: to use a card (KNIGHT) that does not receive game specific options (student ids, ecc...), and then to
     * test only when that card is on the table
     */
    @RepeatedTest(100)
    void notifyClients() {
        ArrayList<ServerClientConnection> connections = new ArrayList<>();
        final boolean[] informationMessageArrived = {false, false};
        // Create a virtual ServerClientConnection that will handle that messages instead of sending them to a socket;
        // in this way I can check the correct messages are sent from the GameObserver, which is invoked directly
        // by the GameController when the game starts
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            connections.add(assertDoesNotThrow(() -> new ServerClientConnection(null) {
                @Override
                public void prepareSocket() {
                    // Don't prepare socket as we don't need it and there's no active socket
                }

                @Override
                public void sendMessage(Message message) {
                    if (message.getType() == MessageTypes.INFORMATION) {
                        informationMessageArrived[finalI] = true;
                    }
                }
            }));
        }

        Map<User, ServerClientConnection> connectionMap = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            connectionMap.put(new User(String.valueOf(i), 2), connections.get(i));
        }

        GameController gameController = new GameController(connectionMap);
        assertDoesNotThrow(() -> gameController.startGame(true));

        Map<Integer, CharacterCard> activeCards = assertDoesNotThrow(() -> gameController.getGameEngine().getTable().getCharacterCards());
        if (activeCards.get(Character.KNIGHT.getId()) != null) // Card is active: test
        {
            // No cards used at start
            for (int i = 0; i < 2; i++) {
                assertFalse(informationMessageArrived[i]);
            }

            // Set the possibility to use instantly the card
            ArrayList<Integer> possibleActions = new ArrayList<>();
            possibleActions.add(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID);
            possibleActions.add(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID);
            gameController.getGameEngine().getRound().setPossibleActions(possibleActions);

            // Give money
            assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(),gameController.getGameEngine().getRound().getCurrentPlayer()).incrementCoins());
            assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(),gameController.getGameEngine().getRound().getCurrentPlayer()).incrementCoins());
            assertDoesNotThrow(()-> CommonManager.takePlayerById(gameController.getGameEngine(),gameController.getGameEngine().getRound().getCurrentPlayer()).incrementCoins());
            // Use a card
            Map<String, String> options = new HashMap<>();
            options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.KNIGHT.getId()));
            assertDoesNotThrow(() -> gameController.resumeGame(gameController.getGameEngine().getRound().getCurrentPlayer(), new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID, options)));

            // Check players warned about card use
            // No cards used at start
            for (int i = 0; i < 2; i++) {
                assertTrue(informationMessageArrived[i]);
            }
        }
    }
}