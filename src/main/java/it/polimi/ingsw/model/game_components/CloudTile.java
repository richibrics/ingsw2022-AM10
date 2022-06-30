package it.polimi.ingsw.model.game_components;

import java.util.ArrayList;

/**
 * Class that describes the CloudTile as a game object, with methods to get information about it and to update its status.
 */
public class CloudTile {
    final private int id;
    final private ArrayList<StudentDisc> students;

    public CloudTile(int id) {
        this.id = id;
        this.students = new ArrayList<>();
    }

    /**
     * Returns the id of the CloudTile
     *
     * @return CloudTile id
     */
    public int getId() {
        return id;
    }

    /**
     * Removes all the students from the CloudTile and returns them
     *
     * @return ArrayList with all the StudentDiscs that were in the CloudTile
     * @see StudentDisc
     */
    public ArrayList<StudentDisc> popStudents() {
        ArrayList<StudentDisc> return_collection = new ArrayList<>(students);
        students.clear();
        return return_collection;
    }

    /**
     * Returns all the students from the CloudTile without editing the students on it
     *
     * @return ArrayList with all the StudentDiscs that are in the CloudTile
     * @see StudentDisc
     */
    public ArrayList<StudentDisc> peekStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Add to the CloudTiles all the passed StudentDiscs
     *
     * @param studentsToAdd ArrayList of StudentDiscs that need to be added to the CloudTile
     * @see StudentDisc
     */
    public void addStudents(ArrayList<StudentDisc> studentsToAdd) {
        students.addAll(studentsToAdd);
    }
}
