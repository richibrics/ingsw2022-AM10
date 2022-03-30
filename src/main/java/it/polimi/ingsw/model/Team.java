package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import it.polimi.ingsw.model.game_components.Tower;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Team {

    private int id;
    private ArrayList<Player> players;
    private ArrayList<Tower> towers;
    private ArrayList<ProfessorPawn> professorTable;

    public Team(int id, ArrayList<Player> players)
    {
        this.id = id;
        this.players = players;
        this.towers = new ArrayList<Tower>();
        this.professorTable = new ArrayList<ProfessorPawn>();
    }

    /**
     * Gets the id of the team.
     * @return the identifier of the team.
     */

    public int getId() {
        return this.id;
    }

    /**
     * Gets the lis of players in the team.
     * @return the players in the team.
     * @see Player
     */

    public ArrayList<Player> getPlayers() { return new ArrayList<Player>(this.players); }

    /**
     * Gets the towers that have not already been built by the players in the team.
     * @return the towers that have not already been built by the team.
     * @see Tower
     */

    public ArrayList<Tower> getTowers() { return new ArrayList<Tower>(this.towers); }

    /**
     * Adds a tower to the list of the towers not built by the team.
     * @param tower the tower to add.
     * @see Tower
     * */

    public void addTower(Tower tower) { this.towers.add(tower); }

    /**
     * Removes a tower from the list of towers that have not been built by the team. If no tower is in the list of towers,
     * throws TowerNotSetException.
     * @return the first element of the towers owned by the team.
     * @throws TowerNotSetException if no tower is available.
     * @see Tower
     */

    public Tower popTower() throws TowerNotSetException
    {
        if (this.towers.isEmpty())
            throw new TowerNotSetException("No tower is available");
        return this.towers.remove(0);
    }

    /**
     * Gets the professor pawns owned by the players in the team.
     * @return the list of professor pawns owned by the team.
     * @see ProfessorPawn
     */

    public ArrayList<ProfessorPawn> getProfessorTable() { return new ArrayList<ProfessorPawn>(this.professorTable); }

    /**
     * Adds a professor pawn to the list of professor pawns owned by the players in the team.
     * @param professorToAdd the professor pawn to add.
     * @see ProfessorPawn
     */

    public void addProfessorPawn(ProfessorPawn professorToAdd) {
        this.professorTable.add(professorToAdd);
    }

    /**
     * Removes from the list of professor pawns owned by the team the professor pawn whose color matches {@code color}.
     * If the pawn is not available, throws a NoSuchElementException.
     * @param color the color of the professor pawn to be removed.
     * @return the professor pawn whose color matches {@code color}.
     * @throws NoSuchElementException if a professor pawn whose color is equal to {@code color} is not found.
     */

    public ProfessorPawn removeProfessorPawn(PawnColor color) throws NoSuchElementException
    {
        int index = -1;
        for (ProfessorPawn pawn : this.professorTable) {
            if (pawn.getColor() == color)
                index = this.professorTable.indexOf(pawn);
        }

        if (index == -1)
            throw new NoSuchElementException("The required professor pawn is not available");

        return this.professorTable.remove(index);
    }
}