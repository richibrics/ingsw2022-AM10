package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.view.game_objects.ClientAssistantCard;

import java.util.ArrayList;

public class ClientPlayer {

    private String username;
    private int coins;
    private int wizard;
    private ArrayList<ClientAssistantCard> assistantCards;

    public ClientPlayer(String username, int coins, int wizard, ArrayList<ClientAssistantCard> assistantCards) {
        this.username = username;
        this.coins = coins;
        this.wizard = wizard;
        this.assistantCards = assistantCards;
    }

    public String getUsername() {
        return this.username;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getWizard() {
        return this.wizard;
    }

    public ArrayList<ClientAssistantCard> getAssistantCards() {
        return this.assistantCards;
    }
}
