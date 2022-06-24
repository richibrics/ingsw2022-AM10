package it.polimi.ingsw.view.exceptions;

public class IllegalUserPreferenceException extends Exception {
    public IllegalUserPreferenceException() {
        super("Illegal user preference");
    }

    public IllegalUserPreferenceException(String message) {
        super(message);
    }
}
