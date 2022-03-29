package model.game_components;

abstract public class CharacterCard {
    final private int id;
    final private int cost;

    public CharacterCard(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }
}
