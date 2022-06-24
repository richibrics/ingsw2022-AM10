package it.polimi.ingsw.controller.observers;

public interface Observer {
    /**
     * Sends the update data with the current state to the clients.
     */
    public void notifyClients();
}
