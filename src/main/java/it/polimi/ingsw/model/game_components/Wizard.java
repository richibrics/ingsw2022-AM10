package it.polimi.ingsw.model.game_components;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that describes the Wizard as a game object, with methods to get information about it and to update its status.
 */
public class Wizard {
    final private int id;
    final private ArrayList<AssistantCard> assistantCards;

    public Wizard(int id, ArrayList<AssistantCard> assistantCards) {
        this.id = id;
        this.assistantCards = new ArrayList<>(assistantCards);
    }

    /**
     * Returns the id of the Wizard
     *
     * @return Wizard id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the AssistantCards available for the Wizard
     *
     * @return Wizard's AssistantCards
     * @see AssistantCard
     */
    public ArrayList<AssistantCard> getAssistantCards() {
        return new ArrayList<>(assistantCards);
    }

    /**
     * Remove an AssistantCard from the Wizard
     *
     * @param assistantCard AssistantCard to remove from the Wizard
     * @throws NoSuchElementException if the requested assistantCard isn't already available in the Wizard
     * @see AssistantCard
     */
    public void removeAssistantCard(AssistantCard assistantCard) throws NoSuchElementException {
        if (!this.assistantCards.remove(assistantCard)) {
            throw new NoSuchElementException("Requested AssistantCard isn't available for this Wizard");
        }
    }
}
