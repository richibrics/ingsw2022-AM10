package it.polimi.ingsw.model.game_components;

/**
 * Class that describes the StudentDisc as a game object, with methods to get information about it.
 */
public class StudentDisc {
    final private PawnColor color;
    final private int id;

    public StudentDisc(int id, PawnColor color) {
        this.id = id;
        this.color = color;
    }

    /**
     * Returns the color (PawnColor) of the StudentDisc
     *
     * @return StudentDisc color
     * @see PawnColor
     */
    public PawnColor getColor() {
        return color;
    }

    /**
     * Returns the id of the StudentDisc
     *
     * @return StudentDisc id
     */
    public int getId() {
        return id;
    }
}
