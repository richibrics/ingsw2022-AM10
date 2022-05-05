package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientCloudTile {

    private int id;
    private ArrayList<Integer> students;

    public ClientCloudTile(int id, ArrayList<Integer> students) {
        this.id = id;
        this.students = students;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Integer> getStudents() {
        return this.students;
    }
}
