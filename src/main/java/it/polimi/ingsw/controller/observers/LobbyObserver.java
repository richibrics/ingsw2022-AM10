package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.Map;
import java.util.Objects;

/**
 * Observer used to warn the players about new lobby status.
 */
public class LobbyObserver implements Observer {
    Map<Integer, Map<User, ServerClientConnection>> clientsWaitingList;

    public LobbyObserver(Map<Integer, Map<User, ServerClientConnection>> clientsWaitingList) {
        this.clientsWaitingList = clientsWaitingList;
    }

    /**
     * Sends the update data with the current lobby state to the clients.
     */
    @Override
    public void notifyClients() {
        this.clientsWaitingList.values().stream().forEach(map -> map.values().stream().filter(Objects::nonNull).forEach(serverClientConnection -> serverClientConnection.sendMessage(Serializer.generateLobbyMessage())));
    }
}
