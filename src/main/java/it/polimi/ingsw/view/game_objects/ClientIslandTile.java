package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.model.game_components.TowerColor;

import java.util.ArrayList;

public class ClientIslandTile {

    private final int id;
    private final ArrayList<Integer> students;
    private final boolean noEntry;
    private final ClientTowerColor tower;

    public ClientIslandTile(int id, ArrayList<Integer> students, boolean noEntry, ClientTowerColor tower) {
        this.id = id;
        this.students = students;
        this.noEntry = noEntry;
        this.tower = tower;
    }

    /**
     * Returns the id of the client IslandTile
     *
     * @return client IslandTile id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets all the students on the client IslandTile
     *
     * @return students on the client IslandTile
     */
    public ArrayList<Integer> getStudents() {
        return this.students;
    }

    /**
     * Gets information about the client IslandTile NoEntry property
     *
     * @return noEntry property
     */
    public boolean hasNoEntry() {
        return this.noEntry;
    }

    /**
     * Gets the current Tower on the client IslandTile
     *
     * @return Tower currently on the client IslandTile
     * @see TowerColor
     */
    public ClientTowerColor getTower() {
        return this.tower;
    }
}