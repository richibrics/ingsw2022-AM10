package it.polimi.ingsw.model.exceptions;

public class TableNotSetException extends Exception {
    public TableNotSetException() {
        super("Table not set in the GameEngine");
    }

    public TableNotSetException(String message) {
        super(message);
    }
}
