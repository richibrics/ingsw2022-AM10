package it.polimi.ingsw.model.game_components;

/**
 * Class that describes the ProfessorPawn as a game object, with methods to get information about it.
 */
public class ProfessorPawn {
    final private PawnColor color;

    public ProfessorPawn(PawnColor color) {
        this.color = color;
    }

    /**
     * Returns the color (PawnColor) of the ProfessorPawn
     *
     * @return Professor color
     * @see PawnColor
     */
    public PawnColor getColor() {
        return color;
    }
}
