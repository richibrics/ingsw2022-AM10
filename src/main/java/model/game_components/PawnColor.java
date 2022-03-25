package model.game_components;

public enum PawnColor {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);

    final private int id;

    PawnColor(int id)
    {
        this.id = id;
    }

    /**
     * Returns the id of the Color value in the enumeration
     *
     * @return      Color id
     */
    public int getId()
    {
        return id;
    }
}
