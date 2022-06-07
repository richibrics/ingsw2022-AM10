package it.polimi.ingsw.view.gui.exceptions;

public class SceneLayoutException extends Exception{

    public SceneLayoutException() {
        super("Scene layout cannot be generated");
    }

    public SceneLayoutException(String message) {
        super(message);
    }
}
