package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.SchoolBoard;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CommonManager {

    /**
     * Returns the school board of the player with {@code playerId}. Throws SchoolBoardNotSetException if the school board
     * has not been set, NoSuchElementException if the required school board could not be found.
     * @param gameEngine the game engine
     * @param playerId the identifier of the player
     * @return the school board of the player with {@code playerId}
     * @throws SchoolBoardNotSetException if the school board has not been set
     * @throws NoSuchElementException if the required school board could not be found
     * @see GameEngine
     * @see Player
     * @see SchoolBoard
     */

    public static SchoolBoard takeSchoolBoardByPlayerId (GameEngine gameEngine, int playerId) throws  SchoolBoardNotSetException, NoSuchElementException
    {
        SchoolBoard schoolBoard = null;
        for (Team team : gameEngine.getTeams()) {
            for (Player player : team.getPlayers())
                if (player.getPlayerId() == playerId)
                    schoolBoard = player.getSchoolBoard();
        }

        if (schoolBoard == null)
            throw new NoSuchElementException("The school board of the player is not available");

        return schoolBoard;
    }

    /**
     * Returns the island tile with {@code islandId}. Throws TableNotSetException if the table
     * has not been set, NoSuchElementException if the required island tile could not be found.
     * @param gameEngine the game engine
     * @param islandId the id of the required island tile
     * @return the required island tile
     * @throws TableNotSetException if the table has not been set
     * @throws NoSuchElementException if the required island tile could not be found
     */

    public static IslandTile takeIslandTileById (GameEngine gameEngine, int islandId) throws TableNotSetException, NoSuchElementException
    {
        IslandTile islandTile = null;
        for (ArrayList<IslandTile> islandGroup : gameEngine.getTable().getIslandTiles()) {
            for (IslandTile island : islandGroup) {
                if (island.getId() == islandId)
                    islandTile = island;
            }
        }

        if (islandTile == null)
            throw new NoSuchElementException("The required island tile could not be found");

        return islandTile;
    }
}