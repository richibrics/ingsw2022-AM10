package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.messages.Message;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that runs a thread which sends STILL_ALIVE messages to the server.
 * The connection with the server is automatically closed if the server doesn't send anything for a certain amount of time.
 */
public class ClientStillAliveChecker implements Runnable {

    private ClientServerConnection clientServerConnection;
    private boolean continueRunning;

    public ClientStillAliveChecker(ClientServerConnection clientServerConnection) {
        this.clientServerConnection = clientServerConnection;
        this.continueRunning = true;
    }

    /**
     * Checks if the server is still running. If the timer has expired it closes the connection, otherwise it decrements the timer
     * and sends a message to the server.
     */

    @Override
    public void run() {
        while (this.continueRunning) {
            try {
                if (this.clientServerConnection.getTimer() <= 0) {
                    this.clientServerConnection.askToCloseConnectionWithError("Server timeout.");
                } else {
                    this.clientServerConnection.decrementTimer();
                    this.clientServerConnection.sendMessage(new Message(MessageTypes.STILL_ALIVE, ""));
                }
                Thread.sleep(NetworkConstants.SLEEP_TIME_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Thread interrupted");
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the continueRunning flag to {@code bool}.
     *
     * @param bool the new value of the flag
     */

    private void setContinueRunning(boolean bool) {
        this.continueRunning = bool;
    }
}