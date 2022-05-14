package it.polimi.ingsw.controller.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {super("The user could not be found");}

    public UserNotFoundException(String message) { super(message); }
}
