package model.exceptions;

public class EmptyBagException extends Exception {
    public EmptyBagException() {
        super("No StudentDisc in the Bag");
    }

    public EmptyBagException(String message) {
        super(message);
    }
}
