package it.polimi.ingsw.view.gui.exceptions;

public class GuiViewNotSet extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public GuiViewNotSet() {
        super("GUI was not set.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public GuiViewNotSet(String message) {
        super(message);
    }
}
