package model.exceptions;

public class IllegalStudentDiscMovementException extends Exception {
    public IllegalStudentDiscMovementException() {
        super("Can't move the StudentDisc as requested");
    }

    public IllegalStudentDiscMovementException(String message) {
        super(message);
    }
}
