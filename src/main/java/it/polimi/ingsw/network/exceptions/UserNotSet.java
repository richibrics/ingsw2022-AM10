package it.polimi.ingsw.network.exceptions;

public class UserNotSet extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public UserNotSet() {
        super("User is not set");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UserNotSet(String message) {
        super(message);
    }
}
