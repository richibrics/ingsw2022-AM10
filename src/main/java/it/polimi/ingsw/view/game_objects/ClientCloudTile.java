package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientCloudTile {

    private final int id;
    private final ArrayList<Integer> students;

    public ClientCloudTile(int id, ArrayList<Integer> students) {
        this.id = id;
        this.students = students;
    }

    /**
     * Returns the id of the client CloudTile
     *
     * @return client CloudTile id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns all the students from the client CloudTile
     *
     * @return students in client CloudTile
     */
    public ArrayList<Integer> getStudents() {
        return this.students;
    }
}
