package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.Map;

public class LobbyHandler {

    private Map<Integer, Map<User, ServerClientConnection>> clientsWaiting;
    private static LobbyHandler lobbyHandler;

    private LobbyHandler() {}

    /**
     * Returns the instance of the LobbyHandler.
     * @return the instance of LobbyHandler
     */

    public static LobbyHandler getLobbyHandler() {
        if (lobbyHandler == null)
            lobbyHandler = new LobbyHandler();
        return lobbyHandler;
    }

    public void addClient(User user, ServerClientConnection serverClientConnection) {

    }

    public void changePreference(String userId, int newPreference) {

    }

    public void generateGame() {

    }

    public void checkIfUsernameIsUsed(String userId) {

    }
}
