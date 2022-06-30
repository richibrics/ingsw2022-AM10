package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.exceptions.TowerAlreadySetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;

import java.util.ArrayList;

/**
 * Class that describes the IslandTile as a game object, with methods to get information about it and to update its status.
 */
public class IslandTile {
    final private int id;
    final private ArrayList<StudentDisc> students;
    private Tower tower;
    private boolean noEntry;

    public IslandTile(int id) {
        this.id = id;
        this.students = new ArrayList<>();
        this.tower = null;
        this.noEntry = false;
    }

    /**
     * Returns the id of the IslandTile
     *
     * @return IslandTile id
     */
    public int getId() {
        return id;
    }

    /**
     * Add to the IslandTile the passed StudentDisc
     *
     * @param studentToAdd StudentDisc that need to be added to the IslandTile
     * @see StudentDisc
     */
    public void addStudent(StudentDisc studentToAdd) {
        this.students.add(studentToAdd);
    }


    /**
     * Get all the students on the IslandTile
     *
     * @return ArrayList with all the IslandTile StudentDiscs
     */
    public ArrayList<StudentDisc> peekStudents() {
        return new ArrayList<>(this.students);
    }

    /**
     * Set a new Tower in the IslandTile and return the old one
     *
     * @param towerToSet Tower to set in the island
     * @return Tower removed from the IslandTile
     * @throws TowerNotSetException if the current IslandTile Tower is null
     * @see Tower
     */
    public Tower replaceTower(Tower towerToSet) throws TowerNotSetException {
        if (this.tower == null)
            throw new TowerNotSetException();
        Tower currentTower = this.tower;
        this.tower = towerToSet;
        return currentTower;
    }

    /**
     * Get the current Tower in the IslandTile
     *
     * @return Tower currently on the IslandTile
     * @throws TowerNotSetException if the current IslandTile Tower is null
     * @see Tower
     */
    public Tower getTower() throws TowerNotSetException {
        if (this.tower == null)
            throw new TowerNotSetException();
        return this.tower;
    }

    /**
     * Set IslandTile tower
     *
     * @param tower Tower to set in the island
     * @see Tower
     */
    public void setTower(Tower tower) throws TowerAlreadySetException {
        if (this.tower != null)
            throw new TowerAlreadySetException();
        this.tower = tower;
    }

    /**
     * Get if there is a Tower in the IslandTile
     *
     * @return True if a Tower is in the IslandTile; false otherwise
     */
    public boolean hasTower() {
        return !(this.tower == null);
    }

    /**
     * Get information about the IslandTile NoEntry property
     *
     * @return noEntry property
     */
    public boolean hasNoEntry() {
        return this.noEntry;
    }

    /**
     * Set IslandTile NoEntry property
     *
     * @param value new NoEntry value
     */
    public void setNoEntry(boolean value) {
        this.noEntry = value;
    }
}
