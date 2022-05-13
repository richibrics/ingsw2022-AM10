package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestLobbyObserver {
    LobbyHandler lobbyHandler;
    LobbyObserver lobbyObserver;

    @BeforeEach
    void setup() {
        lobbyHandler = LobbyHandler.getLobbyHandler();
        lobbyObserver = new LobbyObserver(lobbyHandler.getClientsWaiting());
    }

    /**
     * Tests the observer send the correct message to the correct ServerClientConnections when notifyClients is invoked.
     */
    @Test
    void notifyClients() {
        final boolean[] messageArrived = {false};
        // Create a subclass of ServerClientConnection to simulate the SendMessage method.
        // When the new sendMessage receives the message, checks if the message is correct.
        ServerClientConnection virtualServerClientConnection = assertDoesNotThrow(() -> new ServerClientConnection(null) {
            @Override
            public void prepareSocket() {
                // Don't prepare socket as we don't need it and there's no active socket
            }

            @Override
            public void sendMessage(Message message) {
                assertEquals(Serializer.generateLobbyMessage().getPayload(), message.getPayload());
                messageArrived[0] = true;
            }
        });
        // Register the Server client connection in the Lobby, so will receive the lobby message
        assertDoesNotThrow(() -> lobbyHandler.addClient(new User("1", 2), virtualServerClientConnection));
        // Now with the Observer use notifyClients to send the message and the content will be checked.
        lobbyObserver.notifyClients();
        // Also check that at least once the sendMessage is called (using messageArrived)
        assertTrue(messageArrived[0]);

        // Always reset lobby status if touched after any test, because the lobby is a singleton
        lobbyHandler.emptyMap();
    }
}