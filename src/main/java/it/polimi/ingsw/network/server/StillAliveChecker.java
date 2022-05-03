package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.NetworkConstants;

import java.util.ArrayList;

public class StillAliveChecker implements Runnable {

    private final ArrayList<ServerClientConnection> listOfServerClientConnections;
    private final boolean continueRunning;

    public StillAliveChecker(ArrayList<ServerClientConnection> clientConnections) {
        this.listOfServerClientConnections = clientConnections;
        this.continueRunning = true;
    }

    /**
     * Iterates over the server-client connections checking the timers. If a timer has expired, this method closes the
     * connection with the client, otherwise it decrements the timer and sends a message to the client associated
     * to the timer notifying that the server is running. The check of the timers is done periodically.
     */

    @Override
    public void run() {

        while (this.continueRunning) {
            synchronized (this.listOfServerClientConnections) {
                for (ServerClientConnection serverClientConnection : this.listOfServerClientConnections) {
                    if (serverClientConnection.getTimer() <= 0)
                        serverClientConnection.closeConnection();
                    else {
                        serverClientConnection.decrementTimer();
                        // TODO
                        // serverClientConnection.sendMessage();
                    }
                }
            }
            try {
                Thread.sleep(NetworkConstants.SLEEP_TIME_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}