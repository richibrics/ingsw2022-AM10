package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientCharacterCard {

    private final int id;
    private final int cost;
    private final ArrayList<Integer> storage;

    public ClientCharacterCard(int id, int cost, ArrayList<Integer> storage) {
        this.id = id;
        this.cost = cost;
        this.storage = storage;
    }

    /**
     * Returns the id of the Character of the client CharacterCard.
     *
     * @return the id of the Character of the client CharacterCard
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the cost of the client CharacterCard checking if the Card has already been used.
     *
     * @return the cost of the client CharacterCard
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Returns the Storage of the client CharacterCard
     *
     * @return Storage of client CharacterCard
     */
    public ArrayList<Integer> getStorage() {
        return this.storage;
    }
}