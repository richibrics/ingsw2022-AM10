package it.polimi.ingsw.view.game_objects;

public class ClientBag {

    private final int students;

    public ClientBag(int students) {
        this.students = students;
    }

    /**
     * Returns the number of StudentDiscs in the client Bag
     *
     * @return Number of StudentDiscs in the client Bag
     */

    public int getStudents() {
        return this.students;
    }
}