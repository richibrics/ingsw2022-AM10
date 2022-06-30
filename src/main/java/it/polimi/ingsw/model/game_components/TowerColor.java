package it.polimi.ingsw.model.game_components;

/**
 * Class that enumerates all the TowerColors of the game and contains the methods to get information about them.
 */
public enum TowerColor {
    WHITE(0),
    BLACK(1),
    GREY(2);

    final private int id;

    TowerColor(int id) {
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
