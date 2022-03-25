package model.game_components;

public class MotherNature {
    private IslandTile islandTile;

    public MotherNature(IslandTile islandTile) {
        this.islandTile = islandTile;
    }

    /**
     * Get the current position of MotherNature
     *
     * @return      IslandTile where MotherNature is
     * @see         IslandTile
     */
    public IslandTile getIslandTile()
    {
        return this.islandTile;
    }

    /**
     * Set new MotherNature's position
     *
     * @param newIsland New IslandTile for MotherNature
     * @see         IslandTile
     */
    public void modifyIsland(IslandTile newIsland)
    {
        this.islandTile = newIsland;
    }
}
