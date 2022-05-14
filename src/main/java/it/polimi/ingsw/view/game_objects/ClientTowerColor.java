package it.polimi.ingsw.view.game_objects;

public enum ClientTowerColor {
    WHITE(0),
    BLACK(1),
    GREY(2),
    EMPTY(3);

    final private int id;

    ClientTowerColor(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the TowerColor of client team
     *
     * @return id of the TowerColor of client team.
     */
    public int getId() {
        return this.id;
    }
}
