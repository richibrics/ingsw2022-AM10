package it.polimi.ingsw.view.game_objects;

import java.util.Map;

public class ClientLobby {

    private Map<Integer, Integer> lobbyStatus;

    public ClientLobby(Map<Integer, Integer> lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

    public Map<Integer, Integer> getLobbyStatus() {
        return this.lobbyStatus;
    }
}
