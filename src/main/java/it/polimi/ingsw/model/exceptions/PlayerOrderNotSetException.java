package it.polimi.ingsw.model.exceptions;

public class PlayerOrderNotSetException extends Exception {

    public PlayerOrderNotSetException() {
        super("Order of players' turns in Round not set");
    }

    public PlayerOrderNotSetException(String message) {
        super(message);
    }
}
