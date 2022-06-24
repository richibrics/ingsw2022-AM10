package it.polimi.ingsw.view.exceptions;

public class IllegalGameModeException extends Exception {
    public IllegalGameModeException() {
        super("Invalid game mode");
    }

    public IllegalGameModeException(String message) {
        super(message);
    }
}
