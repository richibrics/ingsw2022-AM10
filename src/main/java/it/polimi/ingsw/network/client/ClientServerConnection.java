package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.exceptions.UserNotSet;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.game_objects.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientServerConnection implements Runnable {

    private final Socket socket;
    private final BufferedReader bufferIn;
    private final PrintWriter bufferOut;
    private final Object syncObject1;
    private final Object syncObject2;
    private final Object syncObject3;
    private final Object syncObject4;
    private final Object syncObject5; // for user
    private final ViewInterface view;
    private final ExecutorService executor;
    private User user;
    private boolean continueReceiving;
    private boolean gameNotStarted;
    private boolean teamsFlag;
    private boolean flagActionMessageIsReady;
    private boolean flagTableReady;
    private boolean flagTeamsReady;
    private ClientRound lastClientRound;
    private ClientTable clientTable;
    private ClientTeams clientTeams;
    private int playerId;
    private int timer;
    private Future future;

    public ClientServerConnection(Socket socket, ViewInterface view) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(NetworkConstants.SOCKET_SO_TIMEOUT_IN_MILLISECONDS);
        this.bufferIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.bufferOut = new PrintWriter(socket.getOutputStream(), true);
        this.view = view;
        this.user = null;
        this.continueReceiving = true;
        this.gameNotStarted = true;
        this.playerId = -1;
        this.teamsFlag = false;
        this.flagActionMessageIsReady = false;
        this.flagTableReady = false;
        this.flagTeamsReady = false;
        this.lastClientRound = null;
        this.clientTable = null;
        this.clientTeams = null;
        this.timer = NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE;
        this.syncObject1 = new Object();
        this.syncObject2 = new Object();
        this.syncObject3 = new Object();
        this.syncObject4 = new Object();
        this.syncObject5 = new Object();
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Waits for handshake message; sends handshake back to the server; sends user to the server; continues to receive
     * messages until a flag is set to false; closes the connection.
     */

    @Override
    public void run() {
        // Wait for the handshake message.
        this.waitForHandshake();
        if (this.getContinueReceiving()) {
            // Send the handshake back to the server. Initial phase completed.
            this.sendHandshake();
            // Send the user to the server.
            this.sendUser();
        }

        // Get messages sent by the server.
        while (this.getContinueReceiving())
            this.receiveMessage();
        // At this point the game has finished or an error has occurred. The connection with the server can be
        // closed.
        try {
            this.closeConnection();
        } catch (IOException e) {
            System.err.println("Unable to close the connection with server");
            return;
        }
    }

    /**
     * Waits for handshake message. Continues to ask the server for handshake message until it receives one. Sends an
     * error message to the server if the content of the json cannot be converted to a Message object.
     */

    private void waitForHandshake() {
        String line = null;
        // Create message to enter in while
        Message message = new Message(MessageTypes.DEFAULT, "");
        while (this.getContinueReceiving() && !message.getType().equals(MessageTypes.HANDSHAKE))
            try {
                // Read line from buffer
                line = this.bufferIn.readLine();
                // Serialize to check type in while
                message = Serializer.fromStringToMessage(line);
                // If a still alive message is received, the timer is
                if (message.getType().equals(MessageTypes.STILL_ALIVE))
                    this.resetTimer();
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                this.setContinueReceiving(false);
            } catch (WrongMessageContentException e) {
                this.askToCloseConnection();
            }
    }

    /**
     * Sends handshake message to the server.
     */

    private void sendHandshake() {
        this.sendMessage(new Message(MessageTypes.HANDSHAKE, NetworkConstants.HANDSHAKE_STRING));
    }

    /**
     * Asks the client for a user and sends the user to the server.
     *
     * @see User
     */

    private void sendUser() {
        synchronized (this.syncObject5) {
            // Ask client for user
            this.future = this.executor.submit(() -> this.view.askForUser());
            // Wait for user. In the meanwhile check if a still alive has arrived. If so, reset the timer
            while (!this.view.userReady() && this.getContinueReceiving()) {
                this.receiveMessage();
            }
            // If everything has gone well get the user and send it to the server
            if (this.getContinueReceiving()) {
                this.user = this.view.getUser();
                // Serialize user and send it to the server
                this.sendMessage(Serializer.fromUserToMessage(this.user));
            }
        }
    }

    /**
     * Checks if a line is in the buffer. If so, calls deserialize, which deserializes the message and acts according
     * to the type of the message. Returns to the caller if a message is not received before the timeout expires. Closes
     * the connection if an IOException is raised.
     */

    private void receiveMessage() {
        String line;
        try {
            // Try to read a line from the buffer
            line = this.bufferIn.readLine();
            // Deserialize the line read from the buffer
            this.deserialize(line);
            // Put the thread to sleep for a while
            Thread.sleep(NetworkConstants.SLEEP_TIME_RECEIVE_MESSAGE_IN_MILLISECONDS);
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            this.askToCloseConnection();
            return;
        } catch (InterruptedException e) {
            return;
        }
    }

    /**
     * Sends {@code message} to the server.
     *
     * @param message the Message object to send to the server
     * @see Message
     */

    public void sendMessage(Message message) {
        synchronized (this.syncObject2) {
            this.bufferOut.println(Serializer.fromMessageToString(message));
            this.bufferOut.flush();
        }
    }

    /**
     * Deserializes the string according to the type of the message contained in the string. Then performs an action, which
     * depends on the type. Below is the list of the actions performed:
     * <ul>
     *   <li>TABLE -> updates the interface;</li>
     *   <li>TEAMS -> saves player id of client when it receives the first TEAMS message. Updates the interface every time it receives
     *                  a TEAMS message;</li>
     *
     *   <li>ROUND -> checks if the current player equals the player id of the client. If so, asks the client to perform one of
     *                  the possible actions;</li>
     *   <li>LOBBY -> displays the content of the lobby;</li>
     *   <li>STILL_ALIVE -> resets the timer which tells if the server has died;</li>
     *   <li>END_GAME -> shows the result of the game and asks to close the connection;</li>
     *   <li>ERROR -> asks the client for a new action (at this stage the server sends this message if it receives a wrong
     *                  action message).</li>
     * <ul>
     *
     * @param string the string to deserialize
     */

    private void deserialize(String string) {
        try {
            Message message = Serializer.fromStringToMessage(string);
            switch (message.getType()) {
                case TABLE -> {
                    // Cancel Thread execution
                    if (future != null) {
                        this.future.cancel(true);
                    }
                    // Get ClientTable object
                    this.clientTable = Serializer.fromMessageToClientTable(message);
                    this.flagTableReady = true;

                    // Display the state of the game if the teams message has already been received
                    if (this.flagTableReady && this.flagTeamsReady) {
                        this.view.displayStateOfGame(this.clientTable, this.clientTeams);
                        this.flagTableReady = false;
                        this.flagTeamsReady = false;
                    }

                    // Check flag gameStarted. If it is true, set it to false
                    if (this.getGameNotStarted())
                        this.setGameNotStarted(false);
                }
                case TEAMS -> {
                    if (future != null) {
                        this.future.cancel(true);
                    }

                    this.clientTeams = Serializer.fromMessageToClientTeams(message);
                    this.flagTeamsReady = true;
                    // Save the player id of the client
                    if (!this.teamsFlag) {
                        this.storePlayerId(this.clientTeams);
                        this.teamsFlag = true;
                    }
                    // Display the state of the game if the table message has already been received
                    if (this.flagTableReady && this.flagTeamsReady) {
                        this.view.displayStateOfGame(this.clientTable, this.clientTeams);
                        this.flagTableReady = false;
                        this.flagTeamsReady = false;
                    }

                    // Check flag gameStarted. If it is true, set it to false
                    if (this.getGameNotStarted())
                        this.setGameNotStarted(false);
                }
                case ROUND -> {
                    this.lastClientRound = Serializer.fromMessageToClientRound(message);
                    this.askAndSendAction();
                }
                case LOBBY -> {
                    ClientLobby clientLobby = Serializer.fromMessageToClientLobby(message);
                    this.view.displayLobby(clientLobby);
                    this.future = this.executor.submit(() -> this.view.askToChangePreference());
                }
                case STILL_ALIVE -> {
                    this.resetTimer();
                }
                case END_GAME -> {
                    this.view.displayWinners(message.getPayload());
                    this.askToCloseConnection();
                }
                case ERROR -> {
                    this.view.showError(message.getPayload());
                    if (this.getGameNotStarted()) {
                        // User error
                        this.view.setUserReady(false);
                        this.sendUser();
                    }
                    else {
                        this.askAndSendAction();
                    }
                }
                default -> {
                }
            }
        } catch (WrongMessageContentException e) {
            this.sendMessage(new Message(MessageTypes.ERROR, e.getMessage()));
        }

    }

    /**
     * Changes the preference of the user to {@code newPreference}.
     *
     * @param newPreference the new preference of the user
     * @throws UserNotSet if the user has not been created yet
     */

    public void changePreference(int newPreference) {
        synchronized (this.syncObject5) {
            this.user.changePreference(newPreference);
            this.sendMessage(Serializer.fromUserToMessage(this.user));
        }
    }

    /**
     * Saves the player id of the client so that the client can easily check if it is their turn.
     *
     * @param clientTeams the ClientTeams message received by the client
     * @see ClientTeams
     */

    private void storePlayerId(ClientTeams clientTeams) {
        synchronized (this.syncObject5) {
            for (ClientTeam clientTeam : clientTeams.getTeams())
                for (ClientPlayer clientPlayer : clientTeam.getPlayers())
                    if (clientPlayer.getUsername().equals(this.user.getId())) {
                        this.playerId = clientPlayer.getPlayerId();
                        return;
                    }
        }
    }

    /**
     * Sets to false the flag that controls the while that checks if a new message has been received by the client.
     */

    public void askToCloseConnection() {
        this.setContinueReceiving(false);
        this.bufferOut.write("\nClosing connection...");
    }

    /**
     * Closes the connection with the server.
     *
     * @throws IOException if something bad happens when closing the buffers
     */

    public void closeConnection() throws IOException {
        this.bufferOut.close();
        this.bufferIn.close();
        this.socket.close();
        this.bufferOut.write("\nConnection closed, goodbye");
        System.exit(0);
    }

    /**
     * Gets the flag that controls the while that checks if a new message has been received by the client.
     *
     * @return the flag
     */

    private boolean getContinueReceiving() {
        synchronized (this.syncObject1) {
            return this.continueReceiving;
        }
    }

    /**
     * Sets to {@code bool} the flag that controls the while that checks if a new message has been received by the client.
     *
     * @param bool the new value of the flag
     */

    private void setContinueReceiving(boolean bool) {
        synchronized (syncObject1) {
            this.continueReceiving = bool;
        }
    }

    /**
     * Gets the timer.
     *
     * @return the timer
     */

    public int getTimer() {
        synchronized (this.syncObject3) {
            return this.timer;
        }
    }

    /**
     * Decrements the timer by one.
     */

    public void decrementTimer() {
        synchronized (this.syncObject3) {
            this.timer -= 1;
        }
    }

    /**
     * Sets the timer to its initial value.
     */

    private void resetTimer() {
        synchronized (this.syncObject3) {
            this.timer = NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE;
        }
    }

    private boolean getGameNotStarted() {
        synchronized (syncObject4) {
            return this.gameNotStarted;
        }
    }

    private void setGameNotStarted(boolean gameNotStarted) {
        synchronized (syncObject4) {
            this.gameNotStarted = gameNotStarted;
        }
    }

    public void setFlagActionMessageIsReady(boolean newValue) {
        synchronized (syncObject2) {
            this.flagActionMessageIsReady = newValue;
        }
    }

    private boolean getFlagActionMessageIsReady() {
        synchronized (syncObject2) {
            return this.flagActionMessageIsReady;
        }
    }


    private void askAndSendAction () {
        if (this.playerId == -1)
            this.askToCloseConnection();
        else if (this.lastClientRound.getCurrentPlayer() == this.playerId) {
            this.view.displayActions(this.lastClientRound.getPossibleActions());
            this.future = this.executor.submit(() -> this.view.showMenu(this.clientTable, clientTeams, this.playerId, this.lastClientRound.getPossibleActions()));
            while (!this.getFlagActionMessageIsReady() && this.getContinueReceiving()) { // TODO Modificare condizione, portare dentro getContinueReceiveing
                this.receiveMessage();
            }
            this.sendMessage(Serializer.fromActionMessageToMessage(this.view.getActionMessage()));
        }

        // Check flag gameStarted. If it is true, set it to false
        if (this.getGameNotStarted())
            this.setGameNotStarted(false);
    }

}