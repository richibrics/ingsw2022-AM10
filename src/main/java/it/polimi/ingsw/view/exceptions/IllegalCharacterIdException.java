package it.polimi.ingsw.view.exceptions;

public class IllegalCharacterIdException extends Exception {
    public IllegalCharacterIdException() {
        super("Invalid character id");
    }

    public IllegalCharacterIdException(String message) {
        super(message);
    }
}
