package it.polimi.ingsw.model.exceptions;

public class TowerAlreadySetException extends Exception {
    public TowerAlreadySetException() {
        super("Tower is already set on the IslandTile");
    }

    public TowerAlreadySetException(String message) {
        super(message);
    }
}
