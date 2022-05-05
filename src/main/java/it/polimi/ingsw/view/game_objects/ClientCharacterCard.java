package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientCharacterCard {

    private int id;
    private int cost;
    private ArrayList<Integer> storage;

    public ClientCharacterCard(int id, int cost, ArrayList<Integer> storage) {
        this.id = id;
        this.cost = cost;
        this.storage = storage;
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }

    public ArrayList<Integer> getStorage() {
        return this.storage;
    }
}