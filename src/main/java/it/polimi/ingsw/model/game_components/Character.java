package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.actions.Action;

public enum Character {
    // When the Actions will be implemented, the Action classes will be replaced here.
    FRIAR(1, 1, 4),
    COOK(2, 2, 0),
    AMBASSADOR(3, 3, 0),
    MAILMAN(4, 1, 0),
    HERBALIST(5, 2, 0),
    CENTAUR(6, 3, 0),
    JESTER(7, 1, 6),
    KNIGHT(8, 2, 0),
    MUSHROOM_HUNTER(9, 3, 0),
    MINSTREL(10, 1, 0),
    LADY(11, 2, 4),
    THIEF(12, 3, 0);

    final private int id;
    final private int cost;
    final private int storageCapacity;

    Character(int id, int cost, int storageCapacity) {
        this.id = id;
        this.cost = cost;
        this.storageCapacity = storageCapacity;
    }

    /**
     * Returns the id of the Character in the enumeration
     *
     * @return Character's id
     */
    final public int getId() {
        return id;
    }

    /**
     * Returns the cost of the Character in the enumeration
     *
     * @return Character's cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the storage capacity of the Character in the enumeration
     *
     * @return Character's storage capacity
     */
    public int getStorageCapacity() {
        return storageCapacity;
    }
}
