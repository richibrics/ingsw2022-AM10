package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientPlayer {

    private final String username;
    private final int playerId;
    private final int coins;
    private final int wizard;
    private final ArrayList<ClientAssistantCard> assistantCards;
    private final ClientAssistantCard lastPlayedAssistantCard;

    public ClientPlayer(String username, int playerId, int coins, int wizard, ArrayList<ClientAssistantCard> assistantCards,
                        ClientAssistantCard lastPlayedAssistantCard) {
        this.username = username;
        this.playerId = playerId;
        this.coins = coins;
        this.wizard = wizard;
        this.assistantCards = assistantCards;
        this.lastPlayedAssistantCard = lastPlayedAssistantCard;
    }

    /**
     * Gets the last played assistant card.
     *
     * @return the last played assistant card
     */

    public ClientAssistantCard getLastPlayedAssistantCard() {
        return this.lastPlayedAssistantCard;
    }

    /**
     * Returns client Username.
     *
     * @return client Username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns client PlayerId
     *
     * @return client PlayerId
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Gets the amount of client coins.
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
