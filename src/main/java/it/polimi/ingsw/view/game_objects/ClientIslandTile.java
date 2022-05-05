package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.model.game_components.TowerColor;

import java.util.ArrayList;

public class ClientIslandTile {

    private int id;
    private ArrayList<Integer> students;
    private boolean noEntry;
    private TowerColor tower;

    public ClientIslandTile(int id, ArrayList<Integer> students, boolean noEntry, TowerColor tower) {
        this.id = id;
        this.students = students;
        this.noEntry = noEntry;
        this.tower = tower;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Integer> getStudents() {
        return this.students;
    }

    public boolean hasNoEntry() {
        return this.noEntry;
    }

    public TowerColor getTower() {
        return this.tower;
    }
}