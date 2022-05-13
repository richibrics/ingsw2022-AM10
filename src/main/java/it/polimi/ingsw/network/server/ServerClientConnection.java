package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.InterruptedGameException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.exceptions.GameControllerNotSetException;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerClientConnection implements Runnable {
    private Socket clientSocket;
    private User user;
    private Integer stillAliveTimer;
    private GameController gameController;
    private boolean continueReceiving;
    private MessageReceivingStep messageReceivingStep;
    private final Object synchronizeStillAliveTimer;
    private final Object synchronizeContinueReceiving;
    private final Object synchronizeMessageReceivingStep;

    private BufferedReader bufferIn;
    private PrintWriter bufferOut;

    public ServerClientConnection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.user = null;
        this.gameController = null;

        // Uses these objects to synchronize on variables that are value indexed, like Integer and Boolean.
        this.synchronizeStillAliveTimer = new Object();
        this.synchronizeContinueReceiving = new Object();
        this.synchronizeMessageReceivingStep = new Object();

        this.resetTimer();

        this.setMessageReceivingStep(MessageReceivingStep.STEP_HANDSHAKE);

        this.prepareSocket();
    }

    /**
     * Prepares socket buffer and timeout; public for testing purposes
     *
     * @throws IOException
     */
    public void prepareSocket() throws IOException {
        this.bufferIn = new BufferedReader(
                new InputStreamReader(this.clientSocket.getInputStream()));
        this.bufferOut = new PrintWriter(this.clientSocket.getOutputStream());
        this.clientSocket.setSoTimeout(NetworkConstants.SOCKET_SO_TIMEOUT_IN_MILLISECONDS);
    }

    /**
     * Main method of the ServerClientConnection thread that receives Clients messages and sort them.
     * Don't call this method directly but use start() instead.
     */
    @Override
    public void run() {
        this.setContinueReceiving(true);
        // Send first Handshake message
        this.sendHandshake();
        // Wait for messages for a long of time
        while (this.getContinueReceiving()) {
            this.receiveMessage();
        }
        // After I don't have to receive any other message, I close the socket and the streams.
        try {
            this.closeConnection();
        } catch (IOException e) {
            System.err.println("Error while closing the client socket");
            e.printStackTrace();
        }
    }

    /**
     * Reads from the Input buffer the next message and passes it to the deserialize method.
     */
    private void receiveMessage() {
        String line = null;
        try {
            line = this.bufferIn.readLine();
        } catch (SocketTimeoutException e) {
            // No message received
            line = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null)
            this.deserialize(line);
        try {
            Thread.sleep(NetworkConstants.SLEEP_TIME_RECEIVE_MESSAGE_IN_MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the Message object to the buffer and sends it.
     *
     * @param message the message that is going to be serialized and sent to the client
     */
    public void sendMessage(Message message) {
        this.bufferOut.println(Serializer.fromMessageToString(message));
        this.bufferOut.flush();
    }

    /**
     * Sets the Connection to stop receiving and to close socket and streams.
     */
    public void askToCloseConnection() {
        this.setContinueReceiving(false);
    }

    /**
     * Closes the Socket and the Streams
     */
    private void closeConnection() throws IOException {
        this.bufferIn.close();
        this.bufferOut.close();
        this.clientSocket.close();
    }

    /**
     * With the passed string, reads the message type and does the correct actions with that message.
     * If the message is a User message, asks the Lobby Handler to update the User object.
     * If the message is a Still Alive message, resets the Still Alive timer.
     * There are some steps in which only a certain type of messages are accepted:
     * first step: only a handshake can be accepted (still_alive also)
     * second step: only a user can be accepted (still_alive also)
     * third step: when game starts, handshake and user are not accepted.
     *
     * @param messageString the message received by the Client, that is going to be deserialized and read
     */
    private void deserialize(String messageString) {
        try {
            Message message = Serializer.fromStringToMessage(messageString);
            switch (message.getType()) {
                case HANDSHAKE -> {
                    if (this.getMessageReceivingStep() == MessageReceivingStep.STEP_HANDSHAKE) {
                        // Then handshake okay, change communication step
                        this.setMessageReceivingStep(MessageReceivingStep.STEP_USER_REGISTER);
                    } else {
                        // Shouldn't receive this type of message in this step: error
                        throw new WrongMessageContentException("Handshake message not allowed in this communication step");
                    }
                }
                case USER -> {
                    if (this.getMessageReceivingStep() == MessageReceivingStep.STEP_USER_REGISTER) {
                        // I received a User message. If it's valid, I register it in the LobbyHandler
                        User newUser = Serializer.fromMessageToUser(message);
                        // Check if username is available with the LobbyHandler. Synchronize here because
                        // if 2 clients ask together I could encounter troubles.
                        if (!LobbyHandler.getLobbyHandler().checkIfUsernameIsUsed(newUser.getId())) {
                            // Then add the user and the connection to the LobbyHandler
                            this.user = newUser;
                            // Set communication step as LOBBY
                            this.setMessageReceivingStep(MessageReceivingStep.STEP_LOBBY);
                            // Add the client. If this generates a Game, a GameController is set in this class
                            // and  also the step becomes IN_GAME
                            LobbyHandler.getLobbyHandler().addClient(this.user, this);
                        } else {
                            throw new WrongMessageContentException("Username already in use, please choose another one");
                        }
                    } else if (this.getMessageReceivingStep() == MessageReceivingStep.STEP_LOBBY) {
                        // I received a User message. This means I have to set the new user or to change the preference of the User
                        // in the Lobby Handler. Before changing it, I have to check that the username is the same as before.
                        User updatedUser = Serializer.fromMessageToUser(message);
                        if (updatedUser.getId().equals(this.user.getId())) {
                            try {
                                LobbyHandler.getLobbyHandler().changePreference(updatedUser.getId(), updatedUser.getPreference());
                            } catch (Exception e) {
                                throw new WrongMessageContentException("The preference could not be changed.");
                            }
                        } else {
                            throw new WrongMessageContentException("User id cannot change when the player wants to change his lobby preference.");
                        }
                    } else {
                        // Shouldn't receive this type of message in this step: error
                        throw new WrongMessageContentException("User message not allowed in this communication step");
                    }
                }
                case ACTION -> {
                    if (this.getMessageReceivingStep() == MessageReceivingStep.STEP_IN_GAME) {
                        // Convert the payload of the Message (a Json) to an ActionMessage, then asks the GameController to run the Action.
                        ActionMessage actionMessage = Serializer.fromMessageToActionMessage(message);
                        gameController.resumeGame(CommonManager.takePlayerIdByUserId(gameController.getGameEngine(), user.getId()), actionMessage);
                    } else {
                        // Shouldn't receive this type of message in this step: error
                        throw new WrongMessageContentException("Action message not allowed in this communication step");
                    }
                }
                case STILL_ALIVE -> {
                    // I received a Still Alive message from the client, so I reset his timer so show he is still connected.
                    this.resetTimer();
                }
                default -> {
                    throw new WrongMessageContentException("Unknown message type");
                }
            }
            // If no exception is thrown, can send a Success message
            this.notifySuccess(message.getType());
        } catch (WrongMessageContentException e) {
            this.notifyError(e.getMessage());
        } catch (InterruptedGameException e) {
            this.notifyError(e.getClass().getName());
        } catch (IllegalGameActionException e) {
            this.notifyError(e.getMessage());
        } finally {
            // If I'm handshaking, at each message, after which I'm in Handshake again, send the Handshake message
            if (this.getMessageReceivingStep() == MessageReceivingStep.STEP_HANDSHAKE)
                this.sendHandshake();
        }
    }

    /**
     * Sends the handshake message to the client.
     */
    private void sendHandshake() {
        this.sendMessage(new Message(MessageTypes.HANDSHAKE, ""));
    }

    /**
     * Sends the success message to the client for a specific message type.
     *
     * @param type the type of the message that server successfully received
     */
    private void notifySuccess(MessageTypes type) {
        if (type != MessageTypes.STILL_ALIVE)
            this.sendMessage(new Message(MessageTypes.SUCCESS, type.toString()));
    }

    /**
     * Sends the error message to the client for a specific message type.
     *
     * @param message message of the error
     */
    public void notifyError(String message) {
        this.sendMessage(new Message(MessageTypes.ERROR, message));
    }

    /**
     * Resets Still Alive timer to its initial value.
     */
    public void resetTimer() {
        synchronized (this.synchronizeStillAliveTimer) {
            this.stillAliveTimer = NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE;
        }
    }

    /**
     * Returns the previously set GameController.
     *
     * @return the GameController
     * @throws GameControllerNotSetException if the GameController was not set before
     */
    public synchronized GameController getGameController() throws GameControllerNotSetException {
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
    public synchronized void setGameController(GameController gameController) {
        this.gameController = gameController;
        this.setMessageReceivingStep(MessageReceivingStep.STEP_IN_GAME);
    }

    /**
     * Gets the still alive timer.
     *
     * @return the still alive timer
     */

    public int getTimer() {
        synchronized (this.synchronizeStillAliveTimer) {
            return this.stillAliveTimer;
        }
    }

    /**
     * Decrements Still Alive Timer by one
     */
    public void decrementTimer() {
        synchronized (this.synchronizeStillAliveTimer) {
            this.stillAliveTimer -= 1;
        }
    }

    /**
     * Returns the value of the Boolean continueReceiving, needed to check if the Server has to continue listening to messages
     * from the Client
     *
     * @return value of the Boolean continueReceiving
     */
    private boolean getContinueReceiving() {
        synchronized (synchronizeContinueReceiving) {
            return continueReceiving;
        }
    }

    /**
     * Sets the value of the Boolean continueReceiving, needed to check if the Server has to continue listening to messages
     * from the Client
     *
     * @param continueReceiving the new value for continueReceiving
     */
    private void setContinueReceiving(boolean continueReceiving) {
        synchronized (synchronizeContinueReceiving) {
            this.continueReceiving = continueReceiving;
        }
    }

    /**
     * Returns the value of the enumeration messageReceivingStep, needed to filter the received messages in the different
     * communication steps.
     *
     * @return the value of the enumeration messageReceivingStep
     */
    private MessageReceivingStep getMessageReceivingStep() {
        synchronized (this.synchronizeMessageReceivingStep) {
            return messageReceivingStep;
        }
    }

    /**
     * Sets the value of the enumeration messageReceivingStep, needed to filter the received messages in the different
     * communication steps.
     *
     * @param messageReceivingStep the new value for messageReceivingStep
     */
    private void setMessageReceivingStep(MessageReceivingStep messageReceivingStep) {
        synchronized (this.synchronizeMessageReceivingStep) {
            this.messageReceivingStep = messageReceivingStep;
        }
    }
}

