package it.polimi.ingsw.view.gui.exceptions;

public class CurrentSceneControllerNotSetException extends Exception {
    /**
     * Constructs a new exception with its default message.
     */
    public CurrentSceneControllerNotSetException() {
        super("Current scene controller not set.");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     */
    public CurrentSceneControllerNotSetException(String message) {
        super(message);
    }
}
