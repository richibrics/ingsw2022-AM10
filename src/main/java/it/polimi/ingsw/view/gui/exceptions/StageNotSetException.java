package it.polimi.ingsw.view.gui.exceptions;

public class StageNotSetException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public StageNotSetException() {
        super("Stage was not set.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public StageNotSetException(String message) {
        super(message);
    }
}
