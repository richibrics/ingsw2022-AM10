package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientSchoolBoard {

    private ArrayList<Integer> entrance;
    private ArrayList<ArrayList<Integer>> diningRoom;

    public ClientSchoolBoard(ArrayList<Integer> entrance, ArrayList<ArrayList<Integer>> diningRoom) {
        this.entrance = entrance;
        this.diningRoom = diningRoom;
    }

    public ArrayList<Integer> getEntrance() {
        return this.entrance;
    }

    public ArrayList<ArrayList<Integer>> getDiningRoom() {
        return this.diningRoom;
    }
}
