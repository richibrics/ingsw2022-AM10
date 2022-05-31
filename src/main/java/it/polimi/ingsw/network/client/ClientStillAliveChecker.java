package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.messages.Message;

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
                if (this.clientServerConnection.getTimer() <= 0)
                    this.clientServerConnection.askToCloseConnection();
                else {
                    this.clientServerConnection.decrementTimer();
                    this.clientServerConnection.sendMessage(new Message(MessageTypes.STILL_ALIVE, ""));
                }
                Thread.sleep(NetworkConstants.SLEEP_TIME_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted");
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the continueRunning flag to {@code bool}.
     * @param bool the new value of the flag
     */

    private void setContinueRunning(boolean bool) {
        this.continueRunning = bool;
    }
}