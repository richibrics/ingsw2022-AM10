package it.polimi.ingsw.model.exceptions;

public class SchoolBoardNotSetException extends Exception {
    public SchoolBoardNotSetException() {
        super("School board not set");
    }

    public SchoolBoardNotSetException(String message) {
        super(message);
    }
}
