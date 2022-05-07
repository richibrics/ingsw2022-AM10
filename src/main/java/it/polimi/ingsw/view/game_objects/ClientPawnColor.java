package it.polimi.ingsw.view.game_objects;

public enum ClientPawnColor {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);

    final private int id;

    ClientPawnColor(int id) {
        this.id = id;
    }

    /**
     * Returns the id of the Color value in the enumeration
     *
     * @return Color id
     */

    public int getId() {
        return id;
    }
}




