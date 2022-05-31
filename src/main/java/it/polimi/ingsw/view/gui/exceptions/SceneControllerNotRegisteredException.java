package it.polimi.ingsw.view.gui.exceptions;

public class SceneControllerNotRegisteredException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public SceneControllerNotRegisteredException() {
        super("Requested scene controller could not be found.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SceneControllerNotRegisteredException(String message) {
        super(message);
    }
}
