package it.polimi.ingsw.view.cli.exceptions;

public class NoSpaceForIslandGroupException extends Exception {

    public NoSpaceForIslandGroupException() {
        super("No space for island group");
    }

    public NoSpaceForIslandGroupException(String message) {
        super(message);
    }
}
