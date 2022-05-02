package it.polimi.ingsw.network.exceptions;

public class GameControllerNotSetException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public GameControllerNotSetException() {
        super("Game controller is not set");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public GameControllerNotSetException(String message) {
        super(message);
    }
}
