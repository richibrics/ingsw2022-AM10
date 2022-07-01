package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

/**
 * Class that runs a thread which sends STILL_ALIVE messages to all the connected clients.
 * Clients connections are automatically closed if the client doesn't send anything for a certain amount of time.
 */
public class ServerStillAliveChecker implements Runnable {

    private final ArrayList<ServerClientConnection> listOfServerClientConnections;
    private final boolean continueRunning;

    public ServerStillAliveChecker(ArrayList<ServerClientConnection> clientConnections) {
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
                for (int j = 0; j < this.listOfServerClientConnections.size(); j++) {
                    ServerClientConnection serverClientConnection = this.listOfServerClientConnections.get(j);
                    if (!serverClientConnection.getContinueReceiving()) {
                        // If connection with client ended, remove it from the StillAliveChecker clients list
                        this.listOfServerClientConnections.remove(j);
                        j--;
                    } else {
                        // Else do the timer calculations
                        if (serverClientConnection.getTimer() <= 0) {
                            serverClientConnection.askToCloseConnection();
                        } else {
                            serverClientConnection.decrementTimer();
                            serverClientConnection.sendMessage(new Message(MessageTypes.STILL_ALIVE, ""));
                        }
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