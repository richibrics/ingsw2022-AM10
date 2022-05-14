package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestGameObserver {
    /**
     * Tests the observer send the correct messages to the correct ServerClientConnections when notifyClients is invoked.
     * This test doesn't invoke directly the observer but also checks the observer is invoked by the game controller
     * when the game starts.
     * Then checks that if the table is not already set, the observer doesn't send anything, even if notified.
     */
    @Test
    void notifyClients() {
        ArrayList<ServerClientConnection> connections = new ArrayList<>();
        final boolean[] tableMessageArrived = {false, false};
        final boolean[] teamsMessageArrived = {false, false};
        final boolean[] roundMessageArrived = {false, false};
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
                    if (message.getType() == MessageTypes.TABLE) {
                        tableMessageArrived[finalI] = true;
                        return;
                    }
                    if (message.getType() == MessageTypes.TEAMS) {
                        teamsMessageArrived[finalI] = true;
                        return;
                    }
                    if (message.getType() == MessageTypes.ROUND) {
                        roundMessageArrived[finalI] = true;
                        return;
                    }
                }
            }));
        }


        Map<User, ServerClientConnection> connectionMap = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            connectionMap.put(new User(String.valueOf(i), 2), connections.get(i));
        }

        GameController gameController = new GameController(connectionMap);
        assertDoesNotThrow(gameController::startGame);

        for (int i = 0; i < 2; i++) {
            assertTrue(tableMessageArrived[i]);
            assertTrue(teamsMessageArrived[i]);
            assertTrue(roundMessageArrived[i]);
        }

        // Now set again the booleans to false and check that any message is sent if the table is not set
        for (int i = 0; i < 2; i++) {
            tableMessageArrived[i]=false;
            teamsMessageArrived[i]=false;
            roundMessageArrived[i]=false;
        }
        // Remove table
        gameController.getGameEngine().setTable(null);
        // Invoke the observer
        GameObserver gameObserver = new GameObserver(connections, gameController.getGameEngine());
        gameObserver.notifyClients();
        // Check the observer didn't call the sendMessage as the table was not set
        for (int i = 0; i < 2; i++) {
            assertFalse(tableMessageArrived[i]);
            assertFalse(teamsMessageArrived[i]);
            assertFalse(roundMessageArrived[i]);
        }

    }
}