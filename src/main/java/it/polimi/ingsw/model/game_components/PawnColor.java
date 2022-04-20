package it.polimi.ingsw.model.game_components;

import java.util.Locale;

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

    /**
     * Checks if the id of the object equals the id of color.
     * @param color instance of PawnColor. this is compared to color
     * @return true if the ids are equal, false otherwise
     */

    public boolean equals(PawnColor color) { return this.getId() == color.getId(); }

    /**
     * Gets a string and converts it to a PawnColor.
     * @param color the name of the color
     * @return the color corresponding to {@code color}
     * @throws Exception if {@code color} is invalid
     */

    public static PawnColor convertStringToPawnColor (String color) throws  Exception {

        PawnColor returnColor = null;

        switch (color.toUpperCase(Locale.ROOT)) {
            case "YELLOW":
                returnColor = PawnColor.YELLOW;
                break;
            case "BLUE" :
                returnColor = PawnColor.BLUE;
                break;
            case "GREEN":
                returnColor = PawnColor.GREEN;
                break;
            case "RED":
                returnColor = PawnColor.RED;
                break;
            case "PINK":
                returnColor = PawnColor.PINK;
                break;
        }

        if (returnColor == null)
            throw new Exception("Invalid string");

        else
            return returnColor;
    }
}
