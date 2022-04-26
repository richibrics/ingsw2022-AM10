package it.polimi.ingsw.controller.exceptions;

public class IllegalGameStateException extends Exception {
    public IllegalGameStateException() {
        super("The game is in a wrong state");
    }

    public IllegalGameStateException(String message) {
        super(message);
    }
}
