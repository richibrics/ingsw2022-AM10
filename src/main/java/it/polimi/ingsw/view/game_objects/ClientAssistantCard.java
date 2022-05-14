package it.polimi.ingsw.view.game_objects;

public class ClientAssistantCard {

    private final int id;
    private final int cardValue;
    private final int movements;

    public ClientAssistantCard(int id, int cardValue, int movements) {
        this.id = id;
        this.cardValue = cardValue;
        this.movements = movements;
    }

    /**
     * Returns the id of client AssistantCard
     *
     * @return client AssistantCard id
     */

    public int getId() {
        return this.id;
    }

    /**
     * Returns the card value of client AssistantCard
     *
     * @return client AssistantCard card value
     */

    public int getCardValue() {
        return this.cardValue;
    }

    /**
     * Returns the movements number of client AssistantCard
     *
     * @return client AssistantCard movements
     */

    public int getMovements() {
        return this.movements;
    }
}
