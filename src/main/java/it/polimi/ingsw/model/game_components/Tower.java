package it.polimi.ingsw.model.game_components;

public class Tower {
    final private TowerColor color;

    public Tower(TowerColor color) {
        this.color = color;
    }

    /**
     * Returns the color (TowerColor) of the Tower
     *
     * @return      Tower color
     * @see         TowerColor
     */
    public TowerColor getColor() {
        return color;
    }
}
