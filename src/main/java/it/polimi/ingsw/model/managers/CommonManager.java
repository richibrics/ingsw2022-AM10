package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.TowerColor;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that contains all the methods that take the game objects using the playerId requested.
 */
public class CommonManager {

    /**
     * Returns the school board of the player with {@code playerId}. Throws SchoolBoardNotSetException if the school board
     * has not been set, NoSuchElementException if the required school board could not be found.
     *
     * @param gameEngine the game engine
     * @param playerId   the identifier of the player
     * @return the school board of the player with {@code playerId}
     * @throws SchoolBoardNotSetException if the school board has not been set
     * @throws NoSuchElementException     if the required school board could not be found
     * @see GameEngine
     * @see Player
     * @see SchoolBoard
     */

    public static SchoolBoard takeSchoolBoardByPlayerId(GameEngine gameEngine, int playerId) throws SchoolBoardNotSetException, NoSuchElementException {
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
     *
     * @param gameEngine the game engine
     * @param islandId   the id of the required island tile
     * @return the required island tile
     * @throws TableNotSetException   if the table has not been set
     * @throws NoSuchElementException if the required island tile could not be found
     */

    public static IslandTile takeIslandTileById(GameEngine gameEngine, int islandId) throws TableNotSetException, NoSuchElementException {
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

    /**
     * Returns the Team id where the Player with {@code playerId} belongs.
     *
     * @param gameEngine the game engine
     * @param playerId   the id of the required player
     * @return the required team id
     * @throws NoSuchElementException if the required player id could not be found
     */
    public static int takeTeamIdByPlayerId(GameEngine gameEngine, int playerId) throws NoSuchElementException {
        for (Team team : gameEngine.getTeams()) {
            for (Player player : team.getPlayers()) {
                if (player.getPlayerId() == playerId)
                    return team.getId();
            }
        }
        throw new NoSuchElementException("The required player could not be found in any team");
    }


    /**
     * Returns the Team with {@code teamId}.
     *
     * @param gameEngine the game engine
     * @param teamId     the id of the required team
     * @return the required team
     * @throws NoSuchElementException if the required team could not be found
     */
    public static Team takeTeamById(GameEngine gameEngine, int teamId) throws NoSuchElementException {
        for (Team team : gameEngine.getTeams()) {
            if (team.getId() == teamId)
                return team;
        }
        throw new NoSuchElementException("The required team could not be found");
    }

    /**
     * Returns the id of the Team that has tower color equal to {@code color}.
     *
     * @param gameEngine the game engine
     * @param color      the color of the towers of the required team
     * @return the required team id
     * @throws NoSuchElementException if the required team could not be found
     * @throws TowerNotSetException   if any Tower had been added to the Team
     */
    public static int takeTeamIdByTowerColor(GameEngine gameEngine, TowerColor color) throws TowerNotSetException {
        for (Team team : gameEngine.getTeams()) {
            if (team.getTeamTowersColor() == color)
                return team.getId();
        }
        throw new NoSuchElementException("The required team could not be found");
    }

    /**
     * Returns the Player with {@code playerId}.
     *
     * @param gameEngine the game engine
     * @param playerId   the id of the required player
     * @return the required player
     * @throws NoSuchElementException if the required player could not be found
     * @see Player
     */
    public static Player takePlayerById(GameEngine gameEngine, int playerId) throws NoSuchElementException {
        for (Team team : gameEngine.getTeams()) {
            for (Player player : team.getPlayers()) {
                if (player.getPlayerId() == playerId)
                    return player;
            }
        }
        throw new NoSuchElementException("The required player could not be found");
    }

    /**
     * Returns the Player whose user id matched {@code userId}.
     *
     * @param gameEngine the game engine
     * @param userId     the id of the required user
     * @return the required player id
     * @throws NoSuchElementException if the required player could not be found
     * @see Player
     * @see User
     */
    public static int takePlayerIdByUserId(GameEngine gameEngine, String userId) throws NoSuchElementException {
        for (Team team : gameEngine.getTeams()) {
            for (Player player : team.getPlayers()) {
                if (player.getUsername().equals(userId))
                    return player.getPlayerId();
            }
        }
        throw new NoSuchElementException("The required player could not be found");
    }


    /**
     * Gets the id of the team given the username of one of the players.
     *
     * @param gameEngine the game engine
     * @param username   the username of the player
     * @return the id of the team containing the player
     * @throws NoSuchElementException if the team could not be found
     */

    public static int getTeamIdByUsername(GameEngine gameEngine, String username) throws NoSuchElementException {
        int teamId = -1;
        for (Team team : gameEngine.getTeams())
            for (Player player : team.getPlayers())
                if (player.getUsername().equals(username))
                    teamId = team.getId();

        if (teamId == -1)
            throw new NoSuchElementException("The team could not be found");

        else return teamId;
    }
}