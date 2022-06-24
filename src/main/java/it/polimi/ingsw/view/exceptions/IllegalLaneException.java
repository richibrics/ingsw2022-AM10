package it.polimi.ingsw.view.exceptions;

public class IllegalLaneException extends  Exception {
    public IllegalLaneException() {
        super("Wrong lane index");
    }

    public IllegalLaneException(String message) {
        super(message);
    }
}
