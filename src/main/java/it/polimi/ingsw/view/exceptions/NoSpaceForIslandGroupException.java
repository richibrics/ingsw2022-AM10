package it.polimi.ingsw.view.exceptions;

public class NoSpaceForIslandGroupException extends Exception {

    public NoSpaceForIslandGroupException() {
        super("No space for island group");
    }

    public NoSpaceForIslandGroupException(String message) {
        super(message);
    }
}
