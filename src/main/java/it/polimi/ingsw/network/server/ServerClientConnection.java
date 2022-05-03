package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.exceptions.GameControllerNotSetException;
import it.polimi.ingsw.network.exceptions.UserNotSet;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientConnection implements Runnable {
    private Socket clientSocket;
    private User user;
    private Integer stillAliveTimer;
    private GameController gameController;
    private boolean continueReceiving;
    private final Object synchronizeStillAliveTimer;

    private final Scanner bufferIn;
    private final PrintWriter bufferOut;

    public ServerClientConnection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.user = null;
        this.gameController = null;
        this.resetTimer();

        this.bufferIn = new Scanner(this.clientSocket.getInputStream());
        this.bufferOut = new PrintWriter(this.clientSocket.getOutputStream());

        // Uses this object to synchronize on variables that are value indexed, like Integer and Boolean.
        this.synchronizeStillAliveTimer = new Object();
    }

    /**
     * Main method of the ServerClientConnection thread that receives Clients messages and sort them.
     * Don't call this method directly but use start() instead.
     */
    @Override
    public void run() {
        this.continueReceiving = true;
        // Request user to the client
        this.requestHandshake();
        this.requestUser();
        // Wait for messages for a long of time
        while (this.continueReceiving) {
            this.receiveMessage();
        }
        // After I don't have to receive any other message, I close the socket and the streams.
        try {
            this.closeConnection();
        } catch (IOException e) {
            System.out.println("Error while closing the client socket");
            e.printStackTrace();
        }
    }

    /**
     * Reads from the Input buffer the next message and passes it to the deserialize method.
     */
    private void receiveMessage() {
        synchronized (this.bufferIn) {
            synchronized (this.bufferOut) {
                String line = this.bufferIn.nextLine();
                this.deserialize(line);
            }
        }
    }

    /**
     * Waits for the handshake with the client. Once the client has sent the correct Handshake message, the method ends.
     */
    private void requestHandshake() {
        synchronized (this.bufferIn) {
            synchronized (this.bufferOut) {
                Message handshakeMessage = new Message(MessageTypes.HANDSHAKE, NetworkConstants.HANDSHAKE_STRING);
                boolean okay = false;
                this.sendMessage(handshakeMessage);
                while (!okay) {
                    if (this.bufferIn.nextLine().equals(Serializer.fromMessageToString(handshakeMessage)))
                        okay = true;
                    else {
                        this.sendMessage(handshakeMessage);
                    }
                }
            }
        }
    }

    /**
     * Waits for the User from the client. Once the client has sent the correct user message, the method ends.
     * This method check if the username is already used, asking it to the Lobby Handler.
     */
    private void requestUser() {
        synchronized (this.bufferIn) {
            synchronized (this.bufferOut) {
                // Wait for a User message from the User.
                Message receivedMessage;
                boolean okay = false;
                while (!okay) {
                    try {
                        receivedMessage = Serializer.fromStringToMessage(this.bufferIn.nextLine());
                        if (receivedMessage.getType() == MessageTypes.USER) {
                            okay = true;
                            User newUser = Serializer.fromMessageToUser(receivedMessage);
                        /* Check if username is available with the LobbyHandler. Synchronize here because
                           if 2 clients ask together I could encounter troubles.*/
                            synchronized (LobbyHandler.getLobbyHandler()) { // TODO Check if safe
                                if (LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed(newUser.getId())) {
                                    // Then add the user and the connection to the LobbyHandler
                                    this.user = newUser;
                                    LobbyHandler.getLobbyHandler().addClient(this.user, this);
                                } else {
                                    this.sendMessage(new Message(MessageTypes.USER_ERROR, "Username already in use, please choose another one"));
                                    okay = false;
                                }
                            }
                        } else {
                            this.sendMessage(new Message(MessageTypes.USER_ERROR, "Expected a User"));
                        }
                    } catch (WrongMessageContentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Writes the Message object to the buffer and sends it.
     *
     * @param message the message that is going to be serialized and sent to the client
     */
    public void sendMessage(Message message) {
        synchronized (this.bufferIn) {
            synchronized (this.bufferOut) {
                this.bufferOut.println(Serializer.fromMessageToString(message));
                this.bufferOut.flush();
            }
        }
    }

    /**
     * Sets the Connection to stop receiving and to close socket and streams.
     */
    public void askToCloseConnection() {
        this.continueReceiving = false;
    }

    /**
     * Closes the Socket and the Streams
     */
    private void closeConnection() throws IOException {
        synchronized (this.bufferIn) {
            synchronized (this.bufferOut) {
                this.bufferIn.close();
                this.bufferOut.close();
                this.clientSocket.close();
            }
        }
    }

    /**
     * With the passed string, reads the message type and does the correct actions with that message.
     * If the message is a User message, asks the Lobby Handler to update the User object.
     * If the message is a Still Alive message, resets the Still Alive timer.
     *
     * @param messageString the message received by the Client, that is going to be deserialized and read
     */
    private void deserialize(String messageString) {
        try {
            Message message = Serializer.fromStringToMessage(messageString);
            switch (message.getType()) {
                case USER -> {
                /* I received a User message. This means I have to change the preference of the User
                   in the Lobby Handler. Before changing it, I have to check that the username is the same as before.
                 */
                    User updatedUser = Serializer.fromMessageToUser(message);
                    if (updatedUser.getId() == this.user.getId()) {
                        LobbyHandler.getLobbyHandler().changePreference(updatedUser.getId(), updatedUser.getPreference());
                    } else {
                        throw new WrongMessageContentException("User id cannot change when the player wants to change his lobby preference.");
                    }
                }
                case STILL_ALIVE -> {
                /*
                I received a Still Alive message from the client, so I reset his timer so show he is still connected.
                 */
                    this.resetTimer();
                }
                case ACTION -> {
                /*
                Convert the payload of the Message (a Json) to an ActionMessage, then asks the GameController to run the Action.
                 */
                    ActionMessage actionMessage = Serializer.fromMessageToActionMessage(message);
                    gameController.resumeGame(CommonManager.takePlayerIdByUserId(gameController.getGameEngine(), user.getId()), actionMessage);
                }
            }
        } catch (WrongMessageContentException e) {
            // TODO Error management
        }
    }

    /**
     * Resets Still Alive timer to its initial value.
     */
    public void resetTimer() {
        synchronized (synchronizeStillAliveTimer) {
            this.stillAliveTimer = NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE;
        }
    }

    /**
     * Returns the previously set GameController.
     *
     * @return the GameController
     * @throws GameControllerNotSetException if the GameController was not set before
     */
    public GameController getGameController() throws GameControllerNotSetException {
        if (gameController == null) {
            throw new GameControllerNotSetException("GameController not set in ServerClientConnection");
        }
        return gameController;
    }

    /**
     * Sets the GameController.
     *
     * @param gameController the GameController to set
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Returns the previously set User.
     *
     * @return the User
     * @throws UserNotSet if the User was not set before
     */
    public User getUser() throws UserNotSet {
        if (user == null) {
            throw new UserNotSet("User not set in ServerClientConnection");
        }
        return user;
    }

    /**
     * Gets the still alive timer.
     * @return the still alive timer
     */

    public int getTimer() {
        return this.stillAliveTimer;
    }

    /**
     * Decrements Still Alive Timer by one
     */
    public void decrementTimer() {
        this.stillAliveTimer -= 1;
    }
}
