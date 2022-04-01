package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.Table;
import it.polimi.ingsw.model.game_components.Tower;
import it.polimi.ingsw.model.game_components.TowerColor;

import java.util.ArrayList;

public class IslandManager extends Manager {
    public IslandManager(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Checks in the IslandTile groups if there are islands with consecutive id (consecutive groups) with the same tower color on it.
     * If so, join the groups together.
     * If in both the groups I have NoEntry tiles, I will set the available NoEntry tiles back to 1
     * If only one group has the NoEntry tile, it will be extended to all the new merged islands
     *
     * @throws TableNotSetException if the Table is not set in the GameEngine
     */
    public void unifyPossibleIslands() throws TableNotSetException {
        Table table = this.getGameEngine().getTable();
        ArrayList<ArrayList<IslandTile>> islandTiles = table.getIslandTiles();
        if(islandTiles.size() > 1)
        {
            for (int groupId = 0; groupId < islandTiles.size() - 1; groupId++) {
                IslandTile islandLeftGroup = islandTiles.get(groupId).get(0);
                IslandTile islandRightGroup = islandTiles.get(groupId + 1).get(0);
                if (islandLeftGroup.hasTower() && islandRightGroup.hasTower()) {
                    try {
                        Tower towerLeftGroup = islandLeftGroup.getTower();
                        Tower towerRightGroup = islandRightGroup.getTower();
                        if (towerLeftGroup.getColor().equals(towerRightGroup.getColor())) {
                            // Unify possible
                            // Manage noEntry before merging
                            this.spreadNoEntryTilesInMergingIslandGroups(groupId,groupId + 1);
                            // Merge
                            islandTiles.get(groupId).addAll(islandTiles.get(groupId + 1));
                            // Clear right group
                            islandTiles.get(groupId + 1).clear();
                            // Remove the right group from the island tiles groups
                            islandTiles.remove(islandTiles.get(groupId + 1));
                            // Now I will check the left group with the new right group so groupId hasn't to advance
                            groupId -= 1;
                        }
                    } catch (TowerNotSetException e) {
                        // hasTower already checked, can't arrive here
                    }
                }
            }
        }
    }

    /**
     * Merges the NoEntry tile to both the groups of IslandTiles if only one has it; set back a NoEntry tile if both have it
     * (privately tested).
     * This operation is run before island merge because if the groups are still separated I can
     * manage the case with both groups with NoEntry and set the NoEntry tile back to the available tiles.
     *
     * @param firstGroupId the index in the island tiles matrix of the first arrayList with the islands I am merging
     * @param secondGroupId the index in the island tiles matrix of the second arrayList with the islands I am merging
     * @throws  TableNotSetException if the Table is not set in the GameEngine
     */
    private void spreadNoEntryTilesInMergingIslandGroups(int firstGroupId, int secondGroupId) throws TableNotSetException {
        Table table = this.getGameEngine().getTable();
        IslandTile islandFirstGroup = table.getIslandTiles().get(firstGroupId).get(0);
        IslandTile islandSecondGroup = table.getIslandTiles().get(secondGroupId).get(0);
        // if noEntry in left group, check if also in right group
        if(islandFirstGroup.hasNoEntry() && islandSecondGroup.hasNoEntry())
        {
            // Both have new entry, so I only have to set back a NoEntry
            table.increaseAvailableNoEntryTiles();
            return;
        }

        if(islandFirstGroup.hasNoEntry() && !islandSecondGroup.hasNoEntry()){
            for(IslandTile islandTile: table.getIslandTiles().get(secondGroupId))
            {
                islandTile.setNoEntry(true);
            }
            return;
        }

        if(!islandFirstGroup.hasNoEntry() && islandSecondGroup.hasNoEntry()){
            for(IslandTile islandTile: table.getIslandTiles().get(firstGroupId))
            {
                islandTile.setNoEntry(true);
            }
        }
    }


    /**
     * Returns the id of the Island where there's MotherNature.
     * @return MotherNature's IslandTile id
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public int getMotherNatureIslandId() throws TableNotSetException {
        return this.getGameEngine().getTable().getMotherNature().getIslandTile().getId();
    }

    /**
     * Returns true if IslandTile whose id is {@code islandId} has the NoEntry tile.
     *
     * @param islandId  the id of the island to search
     * @return true if NoEntry tile is present
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public boolean islandTileHasNoEntry(int islandId) throws TableNotSetException {
        return CommonManager.takeIslandTileById(this.getGameEngine(),islandId).hasNoEntry();
    }

    /**
     * Sets noEntry value for the IslandId whose id is the same of {@code islandId}.
     *
     * @param islandId  the id of the island to search
     * @param value new value for NoEntry in the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public void setIslandTileNoEntry(int islandId, boolean value) throws TableNotSetException {
        CommonManager.takeIslandTileById(this.getGameEngine(),islandId).setNoEntry(value);
    }

    /**
     * Returns the number of IslandTile groups.
     * @return  the number of IslandTile groups
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public int getIslandGroupsNumber() throws TableNotSetException {
        return this.getGameEngine().getTable().getIslandTiles().size();
    }

    /**
     * Gets the color of the IslandTile with {@code islandId}.
     * @param islandId the id of the IslandTile to search
     * @return the color of the Tower on the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     * @throws TowerNotSetException if there isn't a tower on the IslandTile (check with {@link IslandManager#islandTileHasTower(int)})
     */
    public TowerColor getIslandTowerColor(int islandId) throws TableNotSetException, TowerNotSetException {
        return CommonManager.takeIslandTileById(this.getGameEngine(),islandId).getTower().getColor();
    }

    /**
     * Returns true if there's a Tower on the IslandTile
     * @param islandId the id of the IslandTile to search
     * @return true if there's a Tower on the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public boolean islandTileHasTower(int islandId) throws TableNotSetException {
        return CommonManager.takeIslandTileById(getGameEngine(),islandId).hasTower();
    }
}
