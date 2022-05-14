package it.polimi.ingsw.view.game_objects;

import java.util.Map;

public class ClientLobby {

    private final Map<Integer, Integer> lobbyStatus;


    public ClientLobby(Map<Integer, Integer> lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

    /**
     * Returns the current status of client Lobby
     *
     * @return lobbyStatus of client Lobby
     */
    public Map<Integer, Integer> getLobbyStatus() {
        return this.lobbyStatus;
    }
}
