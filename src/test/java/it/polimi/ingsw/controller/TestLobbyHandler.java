package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestLobbyHandler {

    /**
     * Adds some clients and check they are returned with the getter
     */
    @Test
    void addClient() {
        LobbyHandler.getLobbyHandler().emptyMap();
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("1", 2), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("2", 3), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("3", 3), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("4", 4), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("5", 4), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("6", 4), null));
        Map<Integer, Map<User, ServerClientConnection>> clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(1, clients.get(2).size());
        assertEquals(2, clients.get(3).size());
        assertEquals(3, clients.get(4).size());

        assertThrows(IllegalArgumentException.class, () -> LobbyHandler.getLobbyHandler().addClient(new User("6", 5), null));
    }

    @Test
    void changePreference() {
    }

    @Test
    void checkIfUsernameIsUsed() {
        LobbyHandler.getLobbyHandler().emptyMap();
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("1", 2), new virtualServerClientConnection(null)));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("2", 2), new virtualServerClientConnection(null)));
        Map<Integer, Map<User, ServerClientConnection>> clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(0, clients.get(2).size()); // Match should start
        // Check can't add that username again
        assertTrue(assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("1")));
    }

    @Test
    void emptyMap() {
        LobbyHandler.getLobbyHandler().emptyMap();
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("1", 2), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("2", 3), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("3", 3), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("4", 4), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("5", 4), null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("6", 4), null));
        LobbyHandler.getLobbyHandler().emptyMap();
        Map<Integer, Map<User, ServerClientConnection>> clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(0, clients.get(2).size());
        assertEquals(0, clients.get(3).size());
        assertEquals(0, clients.get(4).size());
    }

    @Test
    void removeDisconnectedUser() {
        Map<Integer, Map<User, ServerClientConnection>> clients;
        User user = new User("1", 2);
        LobbyHandler.getLobbyHandler().emptyMap();
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user, null));
        clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(1, clients.get(2).size());
        LobbyHandler.getLobbyHandler().removeDisconnectedUser(user);
        clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(0, clients.get(2).size());

        assertThrows(NoSuchElementException.class, () -> LobbyHandler.getLobbyHandler().removeDisconnectedUser(user));
    }

    /**
     * Runs a game, stops it using the user username and checks the users received th correct number of END_MATCH messages
     */
    @Test
    void removeActiveGameAndCommunicateWinners() {
        LobbyHandler.getLobbyHandler().emptyMap();

        // Check usernames ready for the new match
        assertFalse(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("a"));
        assertFalse(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("b"));

        virtualServerClientConnection virtualServerClientConnection1 = assertDoesNotThrow(() -> new virtualServerClientConnection(null));
        virtualServerClientConnection virtualServerClientConnection2 = assertDoesNotThrow(() -> new virtualServerClientConnection(null));
        Map<Integer, Map<User, ServerClientConnection>> clients;
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("a", 2), virtualServerClientConnection1));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User("b", 2), virtualServerClientConnection2));
        // Check username is not available anymore
        assertTrue(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("a"));
        assertTrue(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("b"));
        // Check match started
        clients = LobbyHandler.getLobbyHandler().getClientsWaiting();
        assertEquals(0, clients.get(2).size());

        // Check no END_MATCH messages sent
        assertEquals(0, virtualServerClientConnection1.countSentMessages.get(MessageTypes.END_GAME));
        assertEquals(0, virtualServerClientConnection2.countSentMessages.get(MessageTypes.END_GAME));
        // Ask to end the match
        Integer[] winners = {1};
        LobbyHandler.getLobbyHandler().removeActiveGameAndCommunicateWinners("a", winners);
        // Check client warned about the end
        assertEquals(1, virtualServerClientConnection1.countSentMessages.get(MessageTypes.END_GAME));
        assertEquals(1, virtualServerClientConnection2.countSentMessages.get(MessageTypes.END_GAME));

        // Check also username is back available
        assertFalse(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("a"));
        assertFalse(LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed("b"));

        // Ask for user not in match
        assertThrows(NoSuchElementException.class, ()->LobbyHandler.getLobbyHandler().removeActiveGameAndCommunicateWinners("notingame", winners));

    }
}

class virtualServerClientConnection extends ServerClientConnection {
    public Map<MessageTypes, Integer> countSentMessages;

    public virtualServerClientConnection(Socket clientSocket) throws IOException {
        super(clientSocket);
        countSentMessages = new HashMap<>();
        for (int i = 0; i < MessageTypes.values().length; i++) {
            countSentMessages.put(MessageTypes.values()[i], 0);
        }
    }

    @Override
    public void prepareSocket() {
        // Don't prepare socket as we don't need it and there's no active socket
    }

    @Override
    public void sendMessage(Message message) {
        countSentMessages.put(message.getType(), countSentMessages.get(message.getType()) + 1);
    }
}