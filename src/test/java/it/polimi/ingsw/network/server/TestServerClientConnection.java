package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.exceptions.GameControllerNotSetException;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestServerClientConnection {

    /**
     * Tests correct message is placed into the socket buffer
     */
    @Test
    void sendMessage() {
        // To test the sendMessage, I redefine the prepareSocket method in the serverClientConnection
        // to make the buffer independent of the socket and to be able to use it and test sent messages.
        final boolean[] flushDone = {false};
        final boolean[] sendDone = {false};
        Message messageToSend = new Message(MessageTypes.HANDSHAKE, "");
        final String stringOfMessageToSend = Serializer.fromMessageToString(messageToSend);

        ServerClientConnection serverClientConnection = assertDoesNotThrow(() -> new ServerClientConnection(null) {
            @Override
            public void prepareSocket() throws IOException {
                this.bufferOut = new PrintWriter(new Writer() {
                    @Override
                    public void write(char[] cbuf, int off, int len) throws IOException {
                        String buf = new String(cbuf).substring(off, len);
                        if (!buf.contains("\n")) { // Avoid the \n message being checked, I only want to check the message with handshake
                            assertEquals(stringOfMessageToSend, buf); // This tests the correct message is sent
                            sendDone[0] = true;
                        }
                    }

                    @Override
                    public void flush() throws IOException {
                        flushDone[0] = true;
                    }

                    @Override
                    public void close() throws IOException {
                        //  Not needed
                    }
                });
            }
        });

        // Send the message: will the test the assertEquals in "write"
        serverClientConnection.sendMessage(messageToSend);
        // This tests the methods are called
        assertTrue(sendDone[0]);
        assertTrue(flushDone[0]);
    }


    /**
     * Directly tests also:
     * - notifyError
     * - notifySuccess
     */
    @Test
    void deserialize() {
        String already_in_lobby_username = "richi";
        // Reset LobbyHandler - safe testing of Singleton
        LobbyHandler.getLobbyHandler().emptyMap();
        // Add in lobby a user, to check when the username used is already in lobby, and after to start a game.
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(new User(already_in_lobby_username, 2), new TestingServerClientConnection()));

        // Create a ServerClientConnection which methods can be used without having a socket connection
        TestingServerClientConnectionWithAssert socketlessServerClientConnection = assertDoesNotThrow(() -> new TestingServerClientConnectionWithAssert());

        // I pass to the deserialize function, the json of messages that will be handled.
        // The messages are going to be sent to the deserialize method in the game order to test also the
        // message step.

        // Check initial step: STEP_HANDSHAKE
        assertEquals(MessageReceivingStep.STEP_HANDSHAKE, socketlessServerClientConnection.getMessageReceivingStep());

        // Try to register a User and get the error, this isn't the correct moment for the user message
        Message userMessage5 = Serializer.fromUserToMessage(new User("a", 3));
        // Check it answers with an error
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage5));

        // Try to request an action and get the error, this isn't the correct moment for the action message
        ActionMessage actionMessage1 = new ActionMessage(1, new HashMap<>());
        Message aMessage1 = Serializer.fromActionMessageToMessage(actionMessage1);
        // Check it answers with an error
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(aMessage1));

        // Deserialize the handshake; tell to the socketlessServerClientConnection that if receives a message,
        // the message must be of the type I want (not an error, for example)
        final Message handshakeMessage = new Message(MessageTypes.HANDSHAKE, "");
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.SUCCESS);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(handshakeMessage));

        // Check step advanced
        assertEquals(MessageReceivingStep.STEP_USER_REGISTER, socketlessServerClientConnection.getMessageReceivingStep());
        // Give again a handshake and check it sends an error to the SCC (tests WrongMessageContentException)
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(handshakeMessage));

        // Now check: STEP_USER_REGISTER. send a username already in lobby
        Message userMessage0 = Serializer.fromUserToMessage(new User(already_in_lobby_username, 3));
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR); // Check correct status sent to socket for correct deserialize
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage0));

        // Now check with a valid username, the game won't start because the 3 players game is not ready
        Message userMessage1 = Serializer.fromUserToMessage(new User("a", 3));
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.SUCCESS); // Check correct status sent to socket for correct deserialize
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage1));

        // Now I should be in Lobby step, check it and try to send again a user but wrongly (wrongly try to change preference) (ERROR)
        Message userMessage2 = Serializer.fromUserToMessage(new User("a", 5));
        assertEquals(MessageReceivingStep.STEP_LOBBY, socketlessServerClientConnection.getMessageReceivingStep());
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR); // Check correct status sent to socket for correct deserialize
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage2));

        // Now change with a valid preference but a different user id (ERROR)
        Message userMessage4 = Serializer.fromUserToMessage(new User("b", 2));
        assertEquals(MessageReceivingStep.STEP_LOBBY, socketlessServerClientConnection.getMessageReceivingStep());
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR); // Check wrong status sent to socket for wrong deserialize
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage4));

        // Now change with a valid preference, two players, to start the match (a player was already in the 2 players lobby) (ERROR)
        Message userMessage3 = Serializer.fromUserToMessage(new User("a", 2));
        assertEquals(MessageReceivingStep.STEP_LOBBY, socketlessServerClientConnection.getMessageReceivingStep());
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.SUCCESS); // Check wrong status sent to socket for wrong deserialize
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(userMessage3));


        // Check match started
        assertEquals(MessageReceivingStep.STEP_IN_GAME, socketlessServerClientConnection.getMessageReceivingStep());

        // Now, before testing all the action branches, send a STILL_ALIVE message and check the timer is reset
        // Before starting, decrement the timer so I can check is back at it starting value
        socketlessServerClientConnection.decrementTimer();
        assertNotEquals(NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE, socketlessServerClientConnection.getTimer());
        // Okay now I send the message and assert timer value is equal to the initial value
        Message stillAliveMessage = new Message(MessageTypes.STILL_ALIVE, "");
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(stillAliveMessage));
        assertEquals(NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE, socketlessServerClientConnection.getTimer());

        // Now check the messages available in STEP_USER_REGISTER: the Action messages.
        // Action messages can throw: IllegalGameActionException, InterruptedGameException, WrongMessageException

        // Ask for legal action with wrong options
        setServerClientConnectionTurnInRound(socketlessServerClientConnection, "a");
        ActionMessage actionMessage2 = new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, new HashMap<>());
        Message aMessage2 = Serializer.fromActionMessageToMessage(actionMessage2);
        // Check there will be an error caused by the message I'm going to send
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(aMessage2));

        // Ask for illegal action: move mother nature
        setServerClientConnectionTurnInRound(socketlessServerClientConnection, "a");
        ActionMessage actionMessage3 = new ActionMessage(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID, new HashMap<>());
        Message aMessage3 = Serializer.fromActionMessageToMessage(actionMessage3);
        // Check there will be an error caused by the message I'm going to send
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(aMessage3));

        // Valid action should send a SUCCESS message (select a wizard)
        setServerClientConnectionTurnInRound(socketlessServerClientConnection, "a");
        Map<String, String> options1 = new HashMap<>();
        options1.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD, "1");
        ActionMessage actionMessage5 = new ActionMessage(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, options1);
        Message aMessage5 = Serializer.fromActionMessageToMessage(actionMessage5);
        // Check there will be an error caused by the message I'm going to send
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.SUCCESS);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(aMessage5));

        // Last check: an InterruptGameException should be translated to an error message to the client
        setServerClientConnectionTurnInRound(socketlessServerClientConnection, "a");
        ArrayList<Integer> actions = new ArrayList<>();
        actions.add(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID);
        assertDoesNotThrow(() -> socketlessServerClientConnection.getGameController().getGameEngine().getRound().setPossibleActions(actions));
        Map<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "1");
        // To get that for example I can set table to null
        assertDoesNotThrow(() -> socketlessServerClientConnection.getGameController().getGameEngine().setTable(null));
        // Run an action and it will fire the exception
        ActionMessage actionMessage4 = new ActionMessage(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID, options);
        Message aMessage4 = Serializer.fromActionMessageToMessage(actionMessage4);
        // Check there will be an error caused by the message I'm going to send
        socketlessServerClientConnection.setTypeOfMessageToCheck(MessageTypes.ERROR);
        socketlessServerClientConnection.deserialize(Serializer.fromMessageToString(aMessage4));

    }

    /**
     * Tests:
     * - setGameController
     * - getGameController
     */
    @Test
    void setGameController() {
        // Create a ServerClientConnection which methods can be used without having a socket connection
        ServerClientConnection socketlessServerClientConnection = assertDoesNotThrow(() -> new TestingServerClientConnection());

        Map<User, ServerClientConnection> connectionMap = new HashMap<>();
        GameController gameController = new GameController(connectionMap);

        // Get the gameController and get the exception as I haven't set it yet
        assertThrows(GameControllerNotSetException.class, socketlessServerClientConnection::getGameController);

        // Set the gameController and assert that the controller received in get is the same I had set
        socketlessServerClientConnection.setGameController(gameController);
        assertEquals(gameController, assertDoesNotThrow(socketlessServerClientConnection::getGameController));
    }

    /**
     * Tests:
     * - getTimer
     * - decrementTimer
     * - resetTimer
     */
    @Test
    void getTimer() {
        // Create a ServerClientConnection which methods can be used without having a socket connection
        ServerClientConnection socketlessServerClientConnection = assertDoesNotThrow(() -> new TestingServerClientConnection());

        // Check initial timer is correct
        assertEquals(NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE, socketlessServerClientConnection.getTimer());

        // Check if decrement works
        socketlessServerClientConnection.decrementTimer();
        assertEquals(NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE - 1, socketlessServerClientConnection.getTimer());

        // Check if reset works
        socketlessServerClientConnection.resetTimer();
        assertEquals(NetworkConstants.INITIAL_STILL_ALIVE_TIMER_VALUE, socketlessServerClientConnection.getTimer());
    }

    /**
     * Tests:
     * - getContinueReceiving
     * - askToCloseConnection
     */
    @Test
    void getContinueReceiving() {
        // Create a ServerClientConnection which methods can be used without having a socket connection
        ServerClientConnection socketlessServerClientConnection = assertDoesNotThrow(() -> new TestingServerClientConnection());

        // Check initial state
        assertTrue(socketlessServerClientConnection.getContinueReceiving());
        // Check askToCloseConnection sets it to false
        socketlessServerClientConnection.askToCloseConnection();
        assertFalse(socketlessServerClientConnection.getContinueReceiving());

    }

    /**
     * Sets the next player in round to the player associated to the passed serverClientConnection
     *
     * @param serverClientConnection the connection whose player will be searched
     */
    private void setServerClientConnectionTurnInRound(ServerClientConnection serverClientConnection, String userId) {
        GameController gameController = assertDoesNotThrow(serverClientConnection::getGameController);

        ArrayList<Integer> newOrder = new ArrayList<>();
        for (int i = 0; i < gameController.getGameEngine().getNumberOfPlayers(); i++) {
            newOrder.add(((1 + i + assertDoesNotThrow(() -> CommonManager.takePlayerIdByUserId(gameController.getGameEngine(), userId))) % gameController.getGameEngine().getNumberOfPlayers()) + 1);
        }
        gameController.getGameEngine().getRound().setOrderOfPlay(newOrder);
    }
}

/**
 * Works without real connection
 */
class TestingServerClientConnection extends ServerClientConnection {
    public TestingServerClientConnection() throws IOException {
        super(null);
    }

    @Override
    public void prepareSocket() throws IOException {
        // Leave empty to test the ServerClientConnection without a real socket connection
    }

    @Override
    public void sendMessage(Message message) {
        // Leave empty to test the ServerClientConnection without a real socket connection
    }
}

/**
 * Works without real connection and has an assert in sendMessage that checks if the message that should be
 * sent is the same as the type set with setTypeOfMessageToCheck.
 * Only Success and Error can be checked, other message types are ignored
 */
class TestingServerClientConnectionWithAssert extends TestingServerClientConnection {
    MessageTypes typeOfMessageToCheck;

    public TestingServerClientConnectionWithAssert() throws IOException {
        super();
    }

    public void setTypeOfMessageToCheck(MessageTypes typeOfMessageToCheck) {
        this.typeOfMessageToCheck = typeOfMessageToCheck;
    }

    @Override
    public void sendMessage(Message message) {
        // Checks if I have a correct type of message
        // If I have table, round, lobby, discard.
        // If I have a success or an error, check with the assigned type
        if (message.getType() == MessageTypes.ERROR || message.getType() == MessageTypes.SUCCESS) {
            assertEquals(this.typeOfMessageToCheck, message.getType());
            System.out.println(message.getPayload());
        }
    }
};

