package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientTeams {

    /**
     * Checks if it returns the correct parameters of ClientTeams.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void getTeams(int value) {
        ArrayList<ClientTeam> teams = new ArrayList<>();

        ClientTowerColor clientTowerColor = ClientTowerColor.BLACK;
        ArrayList<ClientPawnColor> professorPawns = new ArrayList<>();
        professorPawns.add(ClientPawnColor.YELLOW);
        professorPawns.add(ClientPawnColor.RED);
        int numberOfTowers = value;
        ArrayList<ClientPlayer> players = new ArrayList<>();
        String user = "Wanda";
        int coins = 3;
        int wizard = 2;
        ArrayList<ClientAssistantCard> assistantCards = new ArrayList<>();
        ClientAssistantCard clientAssistantCard = new ClientAssistantCard(value + 1, value / 2, value + 4);
        ClientAssistantCard clientAssistantCard1 = new ClientAssistantCard(value + 2, value / 3, value + 3);
        ClientAssistantCard clientAssistantCard2 = new ClientAssistantCard(value + 3, value / 4, value + 2);
        ClientAssistantCard clientAssistantCard3 = new ClientAssistantCard(value + 4, value / 5, value + 1);
        assistantCards.add(clientAssistantCard);
        assistantCards.add(clientAssistantCard1);
        assistantCards.add(clientAssistantCard2);
        assistantCards.add(clientAssistantCard3);
        ClientPlayer clientPlayer = new ClientPlayer(user, coins, wizard, assistantCards);
        players.add(clientPlayer);
        ClientTeam clientTeam = new ClientTeam(clientTowerColor, professorPawns, numberOfTowers, players);
        teams.add(clientTeam);

        ClientTowerColor clientTowerColor1 = ClientTowerColor.WHITE;
        ArrayList<ClientPawnColor> professorPawns1 = new ArrayList<>();
        professorPawns.add(ClientPawnColor.PINK);
        int numberOfTowers1 = value + 1;
        ArrayList<ClientPlayer> players1 = new ArrayList<>();
        String user1 = "Aloy";
        int coins1 = 2;
        int wizard1 = 1;
        ArrayList<ClientAssistantCard> assistantCards1 = new ArrayList<>();
        ClientAssistantCard clientAssistantCard4 = new ClientAssistantCard(value + 1, value / 2, value + 4);
        ClientAssistantCard clientAssistantCard5 = new ClientAssistantCard(value + 2, value / 3, value + 3);
        ClientAssistantCard clientAssistantCard6 = new ClientAssistantCard(value + 3, value / 4, value + 2);
        ClientAssistantCard clientAssistantCard7 = new ClientAssistantCard(value + 4, value / 5, value + 1);
        assistantCards.add(clientAssistantCard4);
        assistantCards.add(clientAssistantCard5);
        assistantCards.add(clientAssistantCard6);
        assistantCards.add(clientAssistantCard7);
        ClientPlayer clientPlayer1 = new ClientPlayer(user1, coins1, wizard1, assistantCards1);
        players1.add(clientPlayer1);
        ClientTeam clientTeam1 = new ClientTeam(clientTowerColor1, professorPawns1, numberOfTowers1, players1);
        teams.add(clientTeam1);

        ClientTowerColor clientTowerColor2 = ClientTowerColor.GREY;
        ArrayList<ClientPawnColor> professorPawns2 = new ArrayList<>();
        professorPawns.add(ClientPawnColor.GREEN);
        professorPawns.add(ClientPawnColor.BLUE);
        int numberOfTowers2 = value + 2;
        ArrayList<ClientPlayer> players2 = new ArrayList<>();
        String user2 = "Tifa";
        int coins2 = 2;
        int wizard2 = 1;
        ArrayList<ClientAssistantCard> assistantCards2 = new ArrayList<>();
        ClientAssistantCard clientAssistantCard8 = new ClientAssistantCard(value + 1, value / 2, value + 4);
        ClientAssistantCard clientAssistantCard9 = new ClientAssistantCard(value + 2, value / 3, value + 3);
        ClientAssistantCard clientAssistantCard10 = new ClientAssistantCard(value + 3, value / 4, value + 2);
        ClientAssistantCard clientAssistantCard11 = new ClientAssistantCard(value + 4, value / 5, value + 1);
        assistantCards.add(clientAssistantCard8);
        assistantCards.add(clientAssistantCard9);
        assistantCards.add(clientAssistantCard10);
        assistantCards.add(clientAssistantCard11);
        ClientPlayer clientPlayer2 = new ClientPlayer(user2, coins2, wizard2, assistantCards2);
        players2.add(clientPlayer2);
        ClientTeam clientTeam2 = new ClientTeam(clientTowerColor2, professorPawns2, numberOfTowers2, players2);
        teams.add(clientTeam2);

        ClientTeams clientTeams = new ClientTeams(teams);

        assertEquals(clientTeams.getTeams(), teams);
    }
}