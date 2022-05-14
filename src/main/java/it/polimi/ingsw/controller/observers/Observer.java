package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.ArrayList;

public interface Observer {
    /**
     * Sends the update data with the current state to the clients.
     */
    public void notifyClients();
}
