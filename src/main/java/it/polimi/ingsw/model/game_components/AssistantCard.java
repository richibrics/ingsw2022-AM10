package it.polimi.ingsw.model.game_components;

public class AssistantCard {
    final private int id;
    final private int cardValue;
    private int movements;

    public AssistantCard(int id, int cardValue, int movements) {
        this.id = id;
        this.cardValue = cardValue;
        this.movements = movements;
    }

    /**
     * Returns the id of the AssistantCard
     *
     * @return      AssistantCard id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the card value of the AssistantCard
     *
     * @return      AssistantCard card value
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * Returns the movements number of the AssistantCard
     *
     * @return      AssistantCard movements
     */
    public int getMovements() {
        return movements;
    }

    /**
     * Increments by {@code increment} the number of movements of Mother Nature allowed by the card.
     * @param increment the number of movements to add to the current number of movements
     */
    public void incrementMovements(int increment) { this.movements += increment; }
}
