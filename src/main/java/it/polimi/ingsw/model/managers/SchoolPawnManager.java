package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that contains all the methods that change the location of student pawns and professor pawns.
 */

public class SchoolPawnManager extends Manager {

    public SchoolPawnManager(GameEngine gameEngine) { super(gameEngine); }

    /**
     * Draws {@code numOfStudents} student discs from the bag. Throws TableNotSetException if the table is not set, EmptyBagException if it is not possible to
     * draw {@code numOfStudents} student discs.
     * @param numOfStudents the number of student discs to draw from the bag
     * @return the list of student disc drawn
     * @throws TableNotSetException if the table is not set
     * @throws EmptyBagException if the bag does not contain the required number of student discs
     * @see StudentDisc
     * @see Bag
     * @see Table
     */

    public ArrayList<StudentDisc> pickStudentsFromBag(int numOfStudents) throws TableNotSetException, EmptyBagException { return this.getGameEngine().getTable().getBag().drawStudents(numOfStudents); }

    /**
     * Takes {@code numOfStudents} student discs from the bag and moves them to the cloud tile with the id specified by
     * {@code cloudId}. Throws TableNotSetException if the table is not set, EmptyBagException if it is not possible to
     * draw {@code numOfStudents} student discs, NoSuchElementException if the required cloud tile could not be found.
     * @param numOfStudents the number of student discs to move from the bag to the cloud tile
     * @param cloudId the identifier of the cloud tile
     * @throws TableNotSetException if the table is not set
     * @throws EmptyBagException if the bag does not contain the required number of student discs
     * @throws NoSuchElementException if the required cloud tile could not be found
     * @see StudentDisc
     * @see Bag
     * @see CloudTile
     * @see Table
     */

    public void moveStudentsFromBagToCloud(int numOfStudents, int cloudId) throws TableNotSetException, EmptyBagException, NoSuchElementException
    {
        ArrayList<StudentDisc> students = this.pickStudentsFromBag(numOfStudents);
        CloudTile cloud = null;
        for (CloudTile cloudTile : this.getGameEngine().getTable().getCloudTiles()) {
            if (cloudTile.getId() == cloudId)
                cloud = cloudTile;
        }

        if (cloud == null)
            throw new NoSuchElementException("The required cloud tile could not be found");

        cloud.addStudents(students);
    }

    /**
     * Moves the required student disc from the Entrance to the Dining Room of the school board of the player identified by
     * {@code playerId}. Throws SchoolBoardNotSetException if the school board of the player has not been set, NoSuchElementException
     * if the student disc could not be found in the entrance of the school board.
     * @param playerId the identifier of the player who owns the school board
     * @param studentId the identifier of the student disc to move
     * @throws SchoolBoardNotSetException if the school board of the player has not been set
     * @throws NoSuchElementException if the student disc could not be found in the entrance of the school board
     * @see StudentDisc
     * @see SchoolBoard
     * @see Player
     */

    public void moveStudentFromEntranceToDiningRoom(int playerId, int studentId) throws  SchoolBoardNotSetException, NoSuchElementException
    {
        SchoolBoard schoolBoard = CommonManager.takeSchoolBoardByPlayerId(this.getGameEngine(), playerId);
        schoolBoard.addStudentToDiningRoom(schoolBoard.removeStudentFromEntrance(studentId));
    }

    /**
     * Moves the student disc identified by {@code studentId} from the entrance of the school board of the player {@code playerId}
     * to the island with id {@code islandId}. Throws SchoolBoardNotSetException if the school board of the player has not been set, NoSuchElementException
     * if the student disc could not be found in the entrance of the school board or if the required island tile could not be found,
     * TableNotSetException if the table has not been set.
     * @param playerId the identifier of the player who owns the school board
     * @param studentId the identifier of the student disc to move
     * @param islandId the identifier of the island tile where the student disc has to be moved to
     * @throws SchoolBoardNotSetException if the school board of the player has not been set
     * @throws NoSuchElementException if the student disc could not be found in the entrance of the school board or if the required
     * island tile could not be found
     * @throws TableNotSetException if the table has not been set
     * @see StudentDisc
     * @see SchoolBoard
     * @see Player
     * @see IslandTile
     * @see Table
     */

    public void moveStudentFromEntranceToIsland(int playerId, int studentId, int islandId) throws SchoolBoardNotSetException, NoSuchElementException, TableNotSetException
    {
        SchoolBoard schoolBoard = CommonManager.takeSchoolBoardByPlayerId(this.getGameEngine(), playerId);
        CommonManager.takeIslandTileById(this.getGameEngine(), islandId).addStudent(schoolBoard.removeStudentFromEntrance(studentId));
    }

    /**
     * Moves the student discs from the cloud tile specified by {@code cloudId} to the entrance of the school board of the
     * player with id {@code playerId}. Throws SchoolBoardNotSetException if the school board of the player has not been set, NoSuchElementException
     * if the required cloud tile could not be found, TableNotSetException if the table has not been set.
     * @param playerId the identifier of the player who owns the school board
     * @param cloudId the identifier of the cloud tile
     * @throws SchoolBoardNotSetException if the school board of the player has not been set
     * @throws NoSuchElementException if the required cloud tile could not be found
     * @throws TableNotSetException if the table has not been set
     * @see StudentDisc
     * @see SchoolBoard
     * @see Player
     * @see CloudTile
     * @see Table
     */

    public void moveStudentsFromCloudTileToEntrance(int playerId, int cloudId) throws SchoolBoardNotSetException, NoSuchElementException, TableNotSetException
    {
        CloudTile cloud = null;
        for (CloudTile cloudTile : this.getGameEngine().getTable().getCloudTiles()) {
            if (cloudTile.getId() == cloudId)
                cloud = cloudTile;
        }

        if (cloud == null)
            throw  new NoSuchElementException("The required cloud tile could not be found");

        if(cloud.peekStudents().size()==0) // Already removed the students
            throw new NoSuchElementException("The required cloud tile hasn't any student");

        CommonManager.takeSchoolBoardByPlayerId(this.getGameEngine(), playerId).addStudentsToEntrance(cloud.popStudents());
    }

    /**
     * Moves the professor with color {@code color} from the team with {@code losingTeamId} to the team with {@code receivingTeamId}.
     * Throws NoSuchElementException if any of the two teams could not be found.
     * @param receivingTeamId the identifier of the receiving team
     * @param losingTeamId the identifier of the team losing the professor pawn
     * @param color the color of the professor pawn
     * @throws NoSuchElementException if any of the two teams could not be found
     * @see ProfessorPawn
     * @see Team
     */

    public void moveProfessor(int receivingTeamId, int losingTeamId, PawnColor color) throws  NoSuchElementException
    {
        Team receivingTeam = null;
        Team losingTeam = null;
        for (Team team : this.getGameEngine().getTeams()) {
            if (team.getId() == receivingTeamId)
                receivingTeam = team;
            else if (team.getId() == losingTeamId)
                losingTeam = team;
        }

        if (receivingTeam == null || losingTeam == null)
            throw new NoSuchElementException("At least one team could not be found");

        receivingTeam.addProfessorPawn(losingTeam.removeProfessorPawn(color));
    }

    /**
     * Returns the StudentDiscs on the IslandTile whose id is {@code islandId}.
     * @param islandId the id of the Island where the Students have to be grabbed
     * @return  ArrayList with StudentDisc present on the Island
     * @throws TableNotSetException if Table is not set in GameEngine
     * @throws NoSuchElementException if the required island tile could not be found
     */
    public ArrayList<StudentDisc> getIslandStudents(int islandId) throws TableNotSetException, NoSuchElementException {
        return new ArrayList<>(CommonManager.takeIslandTileById(this.getGameEngine(), islandId).peekStudents());
    }
}