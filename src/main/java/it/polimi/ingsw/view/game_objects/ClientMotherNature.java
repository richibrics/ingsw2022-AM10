package it.polimi.ingsw.view.game_objects;

public class ClientMotherNature {

    private final int island;

    public ClientMotherNature(int island) {
        this.island = island;
    }

    /**
     * Gets the current position of client MotherNature
     *
     * @return IslandTile where MotherNature is
     */
    public int getIsland() {
        return this.island;
    }
}
