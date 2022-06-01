package it.polimi.ingsw.view.exceptions;

public class IllegalStudentIdException extends Exception {
    public IllegalStudentIdException() {
        super("Wrong student id");
    }

    public IllegalStudentIdException(String message) {
        super(message);
    }
}
