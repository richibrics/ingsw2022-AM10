package it.polimi.ingsw.model.exceptions;

public class NotEnoughCoinException extends Exception {

    public NotEnoughCoinException() {
        super("Can't acquire the Character card for lack of coins");
    }

    public NotEnoughCoinException(String message) {
        super(message);
    }
}
