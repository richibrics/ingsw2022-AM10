package it.polimi.ingsw.view.game_objects;

public enum ClientTowerColor {
    WHITE(0),
    BLACK(1),
    GREY(2),
    EMPTY(3);

    final private int id;

    ClientTowerColor(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}
