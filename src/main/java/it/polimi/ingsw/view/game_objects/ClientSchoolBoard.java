package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientSchoolBoard {

    private final ArrayList<Integer> entrance;
    private final ArrayList<ArrayList<Integer>> diningRoom;

    public ClientSchoolBoard(ArrayList<Integer> entrance, ArrayList<ArrayList<Integer>> diningRoom) {
        this.entrance = entrance;
        this.diningRoom = diningRoom;
    }

    /**
     * Gets the schoolBoard entrance of the client
     *
     * @return schoolBoard entrance of the client
     */
    public ArrayList<Integer> getEntrance() {
        return this.entrance;
    }

    /**
     * Gets the schoolBoard diningRoom of the client
     *
     * @return schoolBoard diningRoom of the client
     */
    public ArrayList<ArrayList<Integer>> getDiningRoom() {
        return this.diningRoom;
    }
}
