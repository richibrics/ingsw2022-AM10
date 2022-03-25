package model.game_components;

public class ProfessorPawn {
    final private PawnColor color;

    public ProfessorPawn(PawnColor color) {
        this.color = color;
    }

    /**
     * Returns the color (PawnColor) of the ProfessorPawn
     *
     * @return      Professor color
     * @see         PawnColor
     */
    public PawnColor getColor() {
        return color;
    }
}
