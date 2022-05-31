package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientPlayer {

    /**
     * Checks if it returns the correct parameters of ClientPlayer.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        String user = "Aerith";
        int player = 1;
        int coins = 3;
        int wizard = 2;
        ArrayList<ClientAssistantCard> assistantCards = new ArrayList<>();
        ClientAssistantCard clientAssistantCard = new ClientAssistantCard(value + 1, value / 2, value + 4);
        ClientAssistantCard clientAssistantCard1 = new ClientAssistantCard(value + 2, value / 3, value + 3);
        ClientAssistantCard clientAssistantCard2 = new ClientAssistantCard(value + 3, value / 4, value + 2);
        ClientAssistantCard clientAssistantCard3 = new ClientAssistantCard(value + 4, value / 5, value + 1);
        ClientAssistantCard lastPlayedAssistantCard = new ClientAssistantCard(value + 5, value / 6, value);
        assistantCards.add(clientAssistantCard);
        assistantCards.add(clientAssistantCard1);
        assistantCards.add(clientAssistantCard2);
        assistantCards.add(clientAssistantCard3);
        ClientPlayer clientPlayer = new ClientPlayer(user, player, coins, wizard, assistantCards, lastPlayedAssistantCard);
        assertEquals(clientPlayer.getPlayerId(), player);
        assertEquals(clientPlayer.getUsername(), user);
        assertEquals(clientPlayer.getCoins(), coins);
        assertEquals(clientPlayer.getWizard(), wizard);
        assertEquals(clientPlayer.getAssistantCards(), assistantCards);
        assertEquals(clientPlayer.getLastPlayedAssistantCard(), lastPlayedAssistantCard);
    }
}