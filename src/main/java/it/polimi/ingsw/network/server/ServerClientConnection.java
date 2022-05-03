package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.NetworkConstants;

import java.net.Socket;

public class ServerClientConnection implements Runnable {
    private Socket clientSocket;
    private User user;
    private int stillAliveTimer;

    public ServerClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.user = null;
        this.resetTimer();
    }

    /**
     * Main method of the ServerClientConnection thread that receives Clients messages and sort them.
     * Don't call this method directly but use start() instead.
     */
    @Override
    public void run() {

    }

    private void addClientToLobby() {

    }

    private void requestUser() {

    }

    public void sendMessage(String message) {

    }

    public void receiveMessage() {

    }

    public void closeConnection() {

    }

    public void deserialize() {

    }

    /**
     * Gets the still alive timer.
     * @return the still alive timer
     */

    public int getTimer() {
        return this.stillAliveTimer;
    }

    public void decrementTimer() {
        this.stillAliveTimer -= 1;
    }

    /**
     * Resets Still Alive timer to its initial value.
     */
    public void resetTimer() {
        this.stillAliveTimer = NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE;
    }
}
