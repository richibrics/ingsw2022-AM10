package model.game_components;

import model.exceptions.TowerNotSetException;

import java.util.ArrayList;

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
     * @return      IslandTile id
     */
    public int getId() {
        return id;
    }

    /**
     * Add to the IslandTile all the passed StudentDiscs
     *
     * @param   studentsToAdd   ArrayList of StudentDiscs that need to be added to the IslandTile
     * @see         StudentDisc
     */
    public void addStudents(ArrayList<StudentDisc> studentsToAdd)
    {
        this.students.addAll(studentsToAdd);
    }

    /**
     * Set IslandTile tower
     *
     * @param   tower   Tower to set in the island
     * @see         Tower
     */
    public void setTower(Tower tower)
    {
        this.tower = tower;
    }

    /**
     * Set a new Tower in the IslandTile and return the old one
     *
     * @param   towerToSet   Tower to set in the island
     * @return      Tower removed from the IslandTile
     * @throws TowerNotSetException if the current IslandTile Tower is null
     * @see         Tower
     */
    public Tower replaceTower(Tower towerToSet) throws TowerNotSetException {
        if(this.tower == null)
            throw new TowerNotSetException();
        Tower currentTower = this.tower;
        this.setTower(towerToSet);
        return currentTower;
    }

    /**
     * Get the current Tower in the IslandTile
     *
     * @return      Tower currently on the IslandTile
     * @throws TowerNotSetException if the current IslandTile Tower is null
     * @see         Tower
     */
    public Tower getTower() throws TowerNotSetException {
        if(this.tower == null)
            throw new TowerNotSetException();
        return this.tower;
    }

    /**
     * Get if there is a Tower in the IslandTile
     *
     * @return      True if a Tower is in the IslandTile; false otherwise
     */
    public boolean hasTower()
    {
        return !(this.tower == null);
    }
}
