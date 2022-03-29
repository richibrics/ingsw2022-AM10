package it.polimi.ingsw.model.exceptions;

public class TowerNotSetException extends Exception {
    public TowerNotSetException() {
        super("Tower not set on the IslandTile");
    }

    public TowerNotSetException(String message) {
        super(message);
    }
}
