package it.polimi.ingsw.controller.exceptions;

public class WrongMessageContentException extends Exception {
    public WrongMessageContentException() {
        super("Wrong content in the received message");
    }

    public WrongMessageContentException(String message) {
        super(message);
    }
}
