package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientTeam {

    /**
     * Checks if it returns the correct parameters of ClientTeam.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        ClientTowerColor clientTowerColor = ClientTowerColor.BLACK;

        ArrayList<ClientPawnColor> professorPawns = new ArrayList<>();
        professorPawns.add(ClientPawnColor.YELLOW);
        professorPawns.add(ClientPawnColor.RED);

        int numberOfTowers = value;

        ArrayList<ClientPlayer> players = new ArrayList<>();
        String user = "Aerith";
        int playerId = 1;
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
        ClientPlayer clientPlayer = new ClientPlayer(user, playerId, coins, wizard, assistantCards, lastPlayedAssistantCard);
        players.add(clientPlayer);
        String user1 = "Aloy";
        int playerId1 = 2;
        int coins1 = 2;
        int wizard1 = 1;
        ArrayList<ClientAssistantCard> assistantCards1 = new ArrayList<>();
        ClientAssistantCard clientAssistantCard4 = new ClientAssistantCard(value + 1, value / 2, value + 4);
        ClientAssistantCard clientAssistantCard5 = new ClientAssistantCard(value + 2, value / 3, value + 3);
        ClientAssistantCard clientAssistantCard6 = new ClientAssistantCard(value + 3, value / 4, value + 2);
        ClientAssistantCard clientAssistantCard7 = new ClientAssistantCard(value + 4, value / 5, value + 1);
        ClientAssistantCard lastPlayedAssistantCard1 = new ClientAssistantCard(value + 5, value / 6, value);
        assistantCards.add(clientAssistantCard4);
        assistantCards.add(clientAssistantCard5);
        assistantCards.add(clientAssistantCard6);
        assistantCards.add(clientAssistantCard7);
        ClientPlayer clientPlayer1 = new ClientPlayer(user1, playerId1, coins1, wizard1, assistantCards1, lastPlayedAssistantCard1);
        players.add(clientPlayer1);

        ClientTeam clientTeam = new ClientTeam(clientTowerColor, professorPawns, numberOfTowers, players);

        assertEquals(clientTeam.getTowersColor(), clientTowerColor);
        assertEquals(clientTeam.getProfessorPawns(), professorPawns);
        assertEquals(clientTeam.getNumberOfTowers(), numberOfTowers);
        assertEquals(clientTeam.getPlayers(), players);
    }
}