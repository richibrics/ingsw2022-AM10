package it.polimi.ingsw.model.exceptions;

public class WizardNotSetException extends Exception {
    public WizardNotSetException() {
        super("Wizard not set");
    }

    public WizardNotSetException(String message) {
        super(message);
    }
}
