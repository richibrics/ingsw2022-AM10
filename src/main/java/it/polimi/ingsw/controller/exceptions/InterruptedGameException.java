package it.polimi.ingsw.controller.exceptions;

public class InterruptedGameException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public InterruptedGameException() {
        super("Game has been interrupted");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InterruptedGameException(String message) {
        super(message);
    }
}
