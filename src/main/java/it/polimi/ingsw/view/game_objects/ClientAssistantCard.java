package it.polimi.ingsw.view.game_objects;

public class ClientAssistantCard {

    private int id;
    private int cardValue;
    private int movements;

    public ClientAssistantCard(int id, int cardValue, int movements) {
        this.id = id;
        this.cardValue = cardValue;
        this.movements = movements;
    }

    public int getId() {
        return this.id;
    }

    public int getCardValue() {
        return this.cardValue;
    }

    public int getMovements() {
        return this.movements;
    }
}
