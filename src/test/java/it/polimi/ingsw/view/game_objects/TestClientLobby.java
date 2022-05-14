package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientLobby {

    /**
     * Creates a ClientLobby and a new HashMap with the lobbyStatus.
     * Then it checks if the ClientLobby has the correct lobbyStatus.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void getLobbyStatus(int value) {
        HashMap<Integer, Integer> lobbyStatus = new HashMap<>();
        lobbyStatus.put(value + 1, value);
        ClientLobby clientLobby = new ClientLobby(lobbyStatus);
        assertEquals(clientLobby.getLobbyStatus(), lobbyStatus);
    }
}