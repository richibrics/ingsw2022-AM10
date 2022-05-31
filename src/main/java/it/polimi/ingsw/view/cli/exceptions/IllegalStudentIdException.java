package it.polimi.ingsw.view.cli.exceptions;

public class IllegalStudentIdException extends Exception {
    public IllegalStudentIdException() {
        super("Wrong student id");
    }

    public IllegalStudentIdException(String message) {
        super(message);
    }
}
