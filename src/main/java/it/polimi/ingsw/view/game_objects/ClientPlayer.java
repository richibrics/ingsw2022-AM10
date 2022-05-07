package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientPlayer {

    private final String username;
    private final int coins;
    private final int wizard;
    private final ArrayList<ClientAssistantCard> assistantCards;

    public ClientPlayer(String username, int coins, int wizard, ArrayList<ClientAssistantCard> assistantCards) {
        this.username = username;
        this.coins = coins;
        this.wizard = wizard;
        this.assistantCards = assistantCards;
    }

    /**
     * Returns client Username
     *
     * @return client Username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the amount of client coinss.
     *
     * @return the amount of coins owned by the client.
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Gets the wizard selected by the client.
     *
     * @return the wizard selected by the client.
     */
    public int getWizard() {
        return this.wizard;
    }

    /**
     * Returns the client AssistantCards contained in client wizard
     *
     * @return client AssistantCards
     * @see ClientAssistantCard
     */
    public ArrayList<ClientAssistantCard> getAssistantCards() {
        return this.assistantCards;
    }
}
