package it.polimi.ingsw.view.exceptions;

public class IllegalAssistantException extends Exception {
    public IllegalAssistantException() {
        super("Invalid assistant name");
    }

    public IllegalAssistantException(String message) {
        super(message);
    }
}
