package it.polimi.ingsw.view.cli.exceptions;

public class IllegalProfessorIdException extends  Exception {
    public IllegalProfessorIdException() {
        super("Wrong professor id");
    }

    public IllegalProfessorIdException(String message) {
        super(message);
    }
}
