package it.polimi.ingsw.model.exceptions;

public class ActionNotSetException extends Exception {
    public ActionNotSetException() {
        super("Action not set in the CharacterCard");
    }

    public ActionNotSetException(String message) {
        super(message);
    }
}
