package it.polimi.ingsw.view.cli.exceptions;

public class WrongNumberOfPlayersException extends Exception {
    public WrongNumberOfPlayersException() {
        super("Wrong number of players");
    }

    public WrongNumberOfPlayersException(String message) {
        super(message);
    }
}
