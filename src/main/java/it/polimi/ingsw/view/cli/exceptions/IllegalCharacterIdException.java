package it.polimi.ingsw.view.cli.exceptions;

public class IllegalCharacterIdException extends Exception {
    public IllegalCharacterIdException() {
        super("Invalid character id");
    }

    public IllegalCharacterIdException(String message) {
        super(message);
    }
}
