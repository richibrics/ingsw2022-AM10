package it.polimi.ingsw.model.exceptions;

public class IllegalGameActionException extends Exception {
    public IllegalGameActionException() {
        super("Illegal action requested");
    }

    public IllegalGameActionException(String message) {
        super(message);
    }
}
