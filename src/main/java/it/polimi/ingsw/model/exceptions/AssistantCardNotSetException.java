package it.polimi.ingsw.model.exceptions;

public class AssistantCardNotSetException extends Exception{
    public AssistantCardNotSetException() {
        super("Assistant card not set");
    }

    public AssistantCardNotSetException(String message) {
        super(message);
    }
}
