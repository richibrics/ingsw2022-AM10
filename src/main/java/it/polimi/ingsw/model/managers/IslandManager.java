package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.exceptions.TowerAlreadySetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that contains all the methods that change the status of the islandTiles.
 */
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
        if (islandTiles.size() > 1) {
            int startingGroup = islandTiles.size() > 2 ? -1 : 0;
            for (int groupId = startingGroup; groupId < islandTiles.size() - 1; groupId++) { // From -1 to join first and last groups
                int leftGroupId = groupId == -1 ? islandTiles.size() - 1 : groupId;
                int rightGroupId = groupId + 1;
                IslandTile islandLeftGroup = islandTiles.get(leftGroupId).get(0);
                IslandTile islandRightGroup = islandTiles.get(rightGroupId).get(0);
                if (islandLeftGroup.hasTower() && islandRightGroup.hasTower()) {
                    try {
                        Tower towerLeftGroup = islandLeftGroup.getTower();
                        Tower towerRightGroup = islandRightGroup.getTower();
                        if (towerLeftGroup.getColor().equals(towerRightGroup.getColor())) {
                            // Unify possible
                            // Manage noEntry before merging
                            this.spreadNoEntryTilesInMergingIslandGroups(leftGroupId, rightGroupId);
                            // Merge
                            islandTiles.get(leftGroupId).addAll(islandTiles.get(rightGroupId));
                            // Clear right group
                            islandTiles.get(rightGroupId).clear();
                            // Remove the right group from the island tiles groups
                            islandTiles.remove(islandTiles.get(rightGroupId));
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
     * @param firstGroupId  the index in the island tiles matrix of the first arrayList with the islands I am merging
     * @param secondGroupId the index in the island tiles matrix of the second arrayList with the islands I am merging
     * @throws TableNotSetException if the Table is not set in the GameEngine
     */
    private void spreadNoEntryTilesInMergingIslandGroups(int firstGroupId, int secondGroupId) throws TableNotSetException {
        Table table = this.getGameEngine().getTable();
        IslandTile islandFirstGroup = table.getIslandTiles().get(firstGroupId).get(0);
        IslandTile islandSecondGroup = table.getIslandTiles().get(secondGroupId).get(0);
        // if noEntry in left group, check if also in right group
        if (islandFirstGroup.hasNoEntry() && islandSecondGroup.hasNoEntry()) {
            // Both have new entry, so I only have to set back a NoEntry
            table.increaseAvailableNoEntryTiles();
            return;
        }

        if (islandFirstGroup.hasNoEntry() && !islandSecondGroup.hasNoEntry()) {
            this.setIslandGroupNoEntryByIslandId(table.getIslandTiles().get(secondGroupId).get(0).getId(), true);
            return;
        }

        if (!islandFirstGroup.hasNoEntry() && islandSecondGroup.hasNoEntry()) {
            this.setIslandGroupNoEntryByIslandId(table.getIslandTiles().get(firstGroupId).get(0).getId(), true);
            return;
        }
    }


    /**
     * Returns the id of the Island where there's MotherNature.
     *
     * @return MotherNature's IslandTile id
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public int getMotherNatureIslandId() throws TableNotSetException {
        return this.getGameEngine().getTable().getMotherNature().getIslandTile().getId();
    }

    /**
     * Returns true if IslandTile whose id is {@code islandId} has the NoEntry tile.
     *
     * @param islandId the id of the island to search
     * @return true if NoEntry tile is present
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public boolean islandTileHasNoEntry(int islandId) throws TableNotSetException {
        return CommonManager.takeIslandTileById(this.getGameEngine(), islandId).hasNoEntry();
    }

    /**
     * Sets noEntry value for the group of IslandTiles where there's and Island whose id is the same of {@code islandId}.
     *
     * @param islandId the id of the island to search
     * @param value    new value for NoEntry in the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public void setIslandGroupNoEntryByIslandId(int islandId, boolean value) throws TableNotSetException {
        // Get the group of this island tile
        ArrayList<IslandTile> receivingGroup = this.getGameEngine().getTable().getIslandTiles().get(
                this.getIslandGroupNumberFromIslandTileId(islandId)
        );

        // Set the no entry value to all the islands
        for (IslandTile islandTile : receivingGroup) {
            islandTile.setNoEntry(value);
        }
    }

    /**
     * Returns the number of IslandTile groups.
     *
     * @return the number of IslandTile groups
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public int getIslandGroupsNumber() throws TableNotSetException {
        return this.getGameEngine().getTable().getIslandTiles().size();
    }

    /**
     * Gets the color of the IslandTile with {@code islandId}.
     *
     * @param islandId the id of the IslandTile to search
     * @return the color of the Tower on the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     * @throws TowerNotSetException if there isn't a tower on the IslandTile (check with {@link IslandManager#islandTileHasTower(int)})
     */
    public TowerColor getIslandTowerColor(int islandId) throws TableNotSetException, TowerNotSetException {
        return CommonManager.takeIslandTileById(this.getGameEngine(), islandId).getTower().getColor();
    }

    /**
     * Returns true if there's a Tower on the IslandTile.
     *
     * @param islandId the id of the IslandTile to search
     * @return true if there's a Tower on the IslandTile
     * @throws TableNotSetException if Table is not set in GameEngine
     */
    public boolean islandTileHasTower(int islandId) throws TableNotSetException {
        return CommonManager.takeIslandTileById(getGameEngine(), islandId).hasTower();
    }

    /**
     * Returns an ArrayList of IslandTiles id where MotherNature can be placed by the Player whose id is {@code playerId}.
     *
     * @param playerId the id of the Player that wants to move MotherNature
     * @return an ArrayList of IslandTiles id where MotherNature can be placed
     * @throws AssistantCardNotSetException if the Player whose id is {@code playerId} hasn't an AssistantCard in hand which determines the MotherNature movements number.
     * @throws TableNotSetException         if Table is not set in GameEngine
     */
    public ArrayList<Integer> getAvailableIslandTilesForMotherNature(int playerId) throws AssistantCardNotSetException, TableNotSetException {
        ArrayList<Integer> destinationsSet = new ArrayList<>();
        Table table = this.getGameEngine().getTable();
        int movements = this.getGameEngine().getAssistantManager().getMovementsOfAssistantCardInHand(playerId);

        // Normalize movements: avoid double scan a group (movements number too high)
        if (movements > table.getIslandTiles().size())
            movements = table.getIslandTiles().size();

        int motherNaturesIslandId = this.getMotherNatureIslandId();

        // Get the group number where mother nature is
        int motherNaturesGroupNumber = this.getIslandGroupNumberFromIslandTileId(motherNaturesIslandId);

        // I have to scan all the groups of island with max distance = movements from [motherNaturesIslandId]'s group
        for (int i = 1; i <= movements; i++) // scan groups
        {
            int groupNumber = (motherNaturesGroupNumber + i) % table.getIslandTiles().size();
            ArrayList<IslandTile> group = table.getIslandTiles().get(groupNumber);
            // add the ids of all the IslandTiles of this group and add to the results set
            for (IslandTile islandTile : group) {
                destinationsSet.add(islandTile.getId());
            }
        }
        return destinationsSet;
    }

    /**
     * Method to support getAvailableIslandTilesForMotherNature: gets an islandId and returns the index (in the ArrayList of
     * ArrayList of IslandTiles) of the ArrayList of IslandTiles (a group of IslandTiles) that contains it.
     * This method helps getAvailableIslandTilesForMotherNature to avoid islandTiles of the same group of the Island where mother
     * nature is to be available for next mother nature step.
     *
     * @param islandId the id of the IslandTile to search
     * @return index of the IslandTile group
     * @throws TableNotSetException   if Table is not set in GameEngine
     * @throws NoSuchElementException if no group contains an IslandTile with {@code islandId}
     */
    private int getIslandGroupNumberFromIslandTileId(int islandId) throws TableNotSetException, NoSuchElementException {
        Table table = this.getGameEngine().getTable();
        IslandTile targetIslandTile = CommonManager.takeIslandTileById(this.getGameEngine(), islandId);
        for (int groupNumber = 0; groupNumber < table.getIslandTiles().size(); groupNumber++) {
            if (table.getIslandTiles().get(groupNumber).contains(targetIslandTile))
                return groupNumber;
        }
        throw new NoSuchElementException("Required IslandTile can't be found in any group");
    }

    /**
     * Moves a Tower to an IslandTile.
     * Gets the Tower from the Team where {@code playerId} belongs. Then it reaches the destination IslandTile (using {@code islandId}) to
     * place the Tower: if there isn't already any tower, sets it; else grabs the old tower and adds it to the Team
     * where the Tower belongs and places the new Tower.
     *
     * @param playerId the id of the Player whose Team moves the Tower on the IslandTile
     * @param islandId the id of the destination IslandTile from the Tower
     * @throws NoSuchElementException   if requested Player o Island could not be found
     * @throws TowerNotSetException     if the Team where the Player belongs hasn't Towers to move
     * @throws TableNotSetException     if Table is not set in GameEngine
     * @throws TowerAlreadySetException if there's already a Tower with the color of the Tower to move on that IslandTile
     */
    public void moveTowerFromPlayerSchoolBoardToIsland(int playerId, int islandId) throws NoSuchElementException, TowerNotSetException, TableNotSetException, TowerAlreadySetException {
        int teamId = CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), playerId);
        Tower newTower = CommonManager.takeTeamById(getGameEngine(), teamId).popTower();

        // Now I can place the new tower on the island
        IslandTile islandTile = CommonManager.takeIslandTileById(this.getGameEngine(), islandId);

        // Check if the island already contains a tower
        if (islandTile.hasTower()) {
            if (islandTile.getTower().getColor() == newTower.getColor())
                throw new TowerAlreadySetException("You're trying to replace a Tower which is of the same color of the new one");
            // Return the tower to the team it belongs to and then set the new tower
            try {
                // Get the team who has to receive back the tower through th e color of the tower on the IslandTile;
                // then in the IslandTile set the new Tower and receive back the old tower that is added to the Team
                // that receives it
                Team oldIslandInfluenceTeam = CommonManager.takeTeamById(getGameEngine(), CommonManager.takeTeamIdByTowerColor(getGameEngine(), islandTile.getTower().getColor()));
                oldIslandInfluenceTeam.addTower(islandTile.replaceTower(newTower));
            } catch (TowerNotSetException e) {
                // I will never reach this because I checked it
            }
        } else {
            // set the tower
            try {
                islandTile.setTower(newTower);
            } catch (TowerAlreadySetException e) {
                // I will never reach this because I checked it
            }
        }
    }

    /**
     * Moves MotherNature safely to the requested IslandTile (safely because I check if the Player who asked it has
     * enough movements - defined by AssistantCard - to place MotherNature there).
     *
     * @param playerId the id of the Player that asked to move MotherNature
     * @param islandId the id of the IslandTile where MotherNature has to be placed
     * @throws AssistantCardNotSetException if the Player whose id is {@code playerId} hasn't an AssistantCard in hand which determines the MotherNature movements number.
     * @throws TableNotSetException         if Table is not set in GameEngine
     * @throws IllegalArgumentException     if MotherNature can't reach the requested IslandTile
     */
    public void moveMotherNature(int playerId, int islandId) throws IllegalArgumentException, AssistantCardNotSetException, TableNotSetException {
        // At first check if it's possible else throw an exception
        if (!this.getAvailableIslandTilesForMotherNature(playerId).contains(islandId))
            throw new IllegalArgumentException("MotherNature can't be moved on the requested IslandTile");
        // If I can move MotherNature there, move it
        MotherNature motherNature = this.getGameEngine().getTable().getMotherNature();
        motherNature.modifyIsland(CommonManager.takeIslandTileById(this.getGameEngine(), islandId));
    }
}
