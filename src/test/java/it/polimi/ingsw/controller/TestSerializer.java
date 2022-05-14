package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Tower;
import it.polimi.ingsw.model.game_components.TowerColor;
import it.polimi.ingsw.model.game_components.Wizard;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.game_objects.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestSerializer {

    /**
     * Tests string is converted to message, tests exception with wrong strings.
     */
    @Test
    void fromStringToMessage() {
        // Create string in json format
        String string = String.format("{\"type\": \"%s\", \"payload\": \"{\\\"hello\\\": \\\"hi\\\"}\"}", MessageTypes.TABLE);
        // Convert string to message
        Message targetMessage = assertDoesNotThrow(() -> Serializer.fromStringToMessage(string));
        assertEquals(targetMessage.getType(), MessageTypes.TABLE);
        assertEquals(targetMessage.getPayload(), "{\"hello\": \"hi\"}");

        // Check if Serializer.fromStringToMessage throws a WrongMessageContentException

        // Incompatible message
        String wrongString1 = "hello";
        assertThrows(WrongMessageContentException.class, () -> Serializer.fromStringToMessage(wrongString1));

        // Missing json
        String wrongString2 = "";
        assertThrows(WrongMessageContentException.class, () -> Serializer.fromStringToMessage(wrongString2));

        // Wrong fields
        String wrongString3 = "{\"payload\": \"{\\\"hello\\\": \\\"hi\\\"}\"}";
        assertThrows(WrongMessageContentException.class, () -> Serializer.fromStringToMessage(wrongString3));
        String wrongString4 = "{\"type\": \"%s\"}";
        assertThrows(WrongMessageContentException.class, () -> Serializer.fromStringToMessage(wrongString4));
    }

    /**
     * Tests correct string is made out of a message.
     */
    @Test
    void fromMessageToString() {
        Message message = new Message(MessageTypes.TABLE, "{\"hello\"}");
        assertEquals(String.format("{\"type\":\"%s\",\"payload\":\"{\\\"hello\\\"}\"}", MessageTypes.TABLE), Serializer.fromMessageToString(message));
    }

    /**
     * Creates an ActionMessage, inserts it in a message.
     * Then converts the message in ActionMessage and check it has the same fields as the initial ActionMessage.
     */
    @Test
    void fromMessageToActionMessage() {
        // Create action message
        Map<String, String> options = new HashMap<>();
        options.put("color", "red");
        ActionMessage actionMessage = new ActionMessage(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, options);
        // Create message
        Gson gson = new GsonBuilder().create();
        Message message = new Message(MessageTypes.ACTION, gson.toJson(actionMessage));
        // Convert message to action message with Serializer.fromMessageToActonMessage
        ActionMessage targetActionMessage = assertDoesNotThrow(() -> Serializer.fromMessageToActionMessage(message));
        assertEquals(targetActionMessage.getActionId(), ModelConstants.ACTION_CALCULATE_INFLUENCE_ID);
        assertEquals(targetActionMessage.getOptions().get("color"), "red");
    }

    /**
     * Checks correct user is deserialized from a predefined user message string.
     */
    @Test
    void fromMessageToUser() {
        // Create message
        Message message = new Message(MessageTypes.USER, "{\"id\": \"kevin\", \"preference\": 3}");
        // Convert message to user with Serializer.fromMessageToUser
        User user = assertDoesNotThrow(() -> Serializer.fromMessageToUser(message));
        assertEquals(user.getId(), "kevin");
        assertEquals(user.getPreference(), 3);
    }

    /**
     * Checks if the Table message has the correct type and properties.
     */
    @Test
    void generateTableMessage() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 2).setTower(new Tower(TowerColor.BLACK)));
        Message message = assertDoesNotThrow(() -> Serializer.generateTableMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.TABLE, message.getType());

        // Check properties of message payload
        Gson gson = new Gson();
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("schoolBoards"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("bag"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("cloudTiles"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("motherNature"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("islandTiles"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("availableProfessorPawns"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("activeCharacterCards"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("availableNoEntryTiles"));
    }

    /**
     * Checks that the Teams message has the correct type and properties.
     */
    @Test
    void generateTeamsMessage() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        assistantCards.add(new AssistantCard(1, 3, 2));
        player1.setWizard(new Wizard(1, assistantCards));
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(() -> Serializer.generateTeamsMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.TEAMS, message.getType());

        // Check if payload has the correct properties
        Gson gson = new Gson();
        Type type = (new TypeToken<ArrayList<JsonObject>>() {
        }).getType();

        ArrayList<JsonObject> jsonObjects = gson.fromJson(message.getPayload(), type);
        assertTrue(jsonObjects.get(0).has("towersColor"));
        assertTrue(jsonObjects.get(0).has("numberOfTowers"));
        assertTrue(jsonObjects.get(0).has("professorPawns"));
        assertTrue(jsonObjects.get(0).has("players"));
        ArrayList<JsonObject> jsonObjects1 = gson.fromJson(jsonObjects.get(0).get("players"), type);
        assertTrue(jsonObjects1.get(0).has("username"));
        assertTrue(jsonObjects1.get(0).has("coins"));
        assertTrue(jsonObjects1.get(0).has("wizard"));
        assertTrue(jsonObjects1.get(0).has("assistantCards"));
        assertTrue(jsonObjects1.get(0).has("wizard"));
        ArrayList<JsonObject> jsonObjects2 = gson.fromJson(jsonObjects1.get(0).get("assistantCards"), type);
        assertTrue(jsonObjects2.get(0).has("id"));
        assertTrue(jsonObjects2.get(0).has("cardValue"));
        assertTrue(jsonObjects2.get(0).has("movements"));
    }

    /**
     * Checks that the LobbyMessage has the correct type and properties.
     */
    @Test
    void generateLobbyMessage() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 3);
        User user3 = new User("3", 4);
        // Add users to lobby
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user1, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user2, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user3, null));

        Message message = assertDoesNotThrow(() -> Serializer.generateLobbyMessage());
        // Check type
        assertEquals(MessageTypes.LOBBY, message.getType());

        // Check if payload has the correct properties
        Gson gson = new Gson();
        Type type = (new TypeToken<Map<Integer, Integer>>() {
        }).getType();
        Map<Integer, Integer> map = gson.fromJson(message.getPayload(), type);
        assertEquals(1, map.get(2));
        assertEquals(1, map.get(3));
        assertEquals(1, map.get(4));
        LobbyHandler.getLobbyHandler().emptyMap();
    }

    /**
     * Checks that the RoundMessage has the correct type and properties.
     */
    @Test
    void generateRoundMessage() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(() -> Serializer.generateRoundMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.ROUND, message.getType());

        // Check properties
        Gson gson = new Gson();
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("listOfActions"));
        assertTrue(gson.fromJson(message.getPayload(), JsonObject.class).has("currentPlayer"));
    }

    /**
     * Checks that the EndGameMessage has the correct type and checks also if payload is correct.
     */
    @Test
    void generateEndGameMessage() {
        User user1 = new User("kevin", 2);
        User user2 = new User("steph", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(player1);
        winners.add(player2);

        Message message = assertDoesNotThrow(() -> Serializer.generateEndGameMessage(winners));
        // Check type
        assertEquals(MessageTypes.END_GAME, message.getType());
        // Check json in payload
        assertEquals(message.getPayload(), "[\"kevin\",\"steph\"]");
    }

    /**
     * Checks if the ClientTable object has been populated with the content of the Table message.
     */

    @Test
    void fromMessageToClientTable() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        Message message = assertDoesNotThrow(() -> Serializer.generateTableMessage(gameEngine));

        // Check content of ClientTable object
        ClientTable clientTable = assertDoesNotThrow(() -> Serializer.fromMessageToClientTable(message));
        assertEquals(3, clientTable.getSchoolBoards().size());
        assertEquals(9, clientTable.getSchoolBoards().get(0).getEntrance().size());
        assertTrue(clientTable.getBag().getStudents() <= 126 && clientTable.getBag().getStudents() >= 0);
        assertEquals(3, clientTable.getCloudTiles().size());
        assertTrue(clientTable.getMotherNature().getIsland() <= 12 && clientTable.getMotherNature().getIsland() >= 1);
        assertEquals(12, clientTable.getIslandTiles().size());
        assertEquals(5, clientTable.getAvailableProfessorPawns().size());
        assertEquals(3, clientTable.getActiveCharacterCards().size());
        assertEquals(4, clientTable.getAvailableNoEntryTiles());
    }

    /**
     * Checks if the ClientTeams object has been populated with the content of the Teams message.
     */

    @Test
    void fromMessageToClientTeams() {
        User user1 = new User("kevin", 3);
        User user2 = new User("steph", 3);
        User user3 = new User("ja", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        // Set wizard for player 1
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        assistantCards.add(new AssistantCard(1, 3, 2));
        player1.setWizard(new Wizard(1, assistantCards));
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        Message message = assertDoesNotThrow(() -> Serializer.generateTeamsMessage(gameEngine));
        ClientTeams clientTeams = assertDoesNotThrow(() -> Serializer.fromMessageToClientTeams(message));

        // Check content of ClientTeams object
        assertTrue(clientTeams.getTeams().get(0).getTowersColor().equals(ClientTowerColor.BLACK) ||
                clientTeams.getTeams().get(0).getTowersColor().equals(ClientTowerColor.WHITE) ||
                clientTeams.getTeams().get(0).getTowersColor().equals(ClientTowerColor.GREY));
        assertEquals(6, clientTeams.getTeams().get(0).getNumberOfTowers());
        assertEquals(0, clientTeams.getTeams().get(0).getProfessorPawns().size());
        assertEquals("steph", clientTeams.getTeams().get(1).getPlayers().get(0).getUsername());
        assertEquals(3, clientTeams.getTeams().get(2).getPlayers().get(0).getCoins());
        assertEquals(1, clientTeams.getTeams().get(0).getPlayers().get(0).getWizard());
        assertEquals(1, clientTeams.getTeams().get(0).getPlayers().get(0).getAssistantCards().size());
        assertEquals(1, clientTeams.getTeams().get(0).getPlayers().get(0).getAssistantCards().get(0).getId());
        assertEquals(3, clientTeams.getTeams().get(0).getPlayers().get(0).getAssistantCards().get(0).getCardValue());
        assertEquals(2, clientTeams.getTeams().get(0).getPlayers().get(0).getAssistantCards().get(0).getMovements());
    }

    /**
     * Checks if the ClientLobby object has been populated with the content of the Lobby message.
     */

    @Test
    void fromMessageToClientLobby() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 3);
        User user3 = new User("3", 4);
        User user4 = new User("4", 3);
        User user5 = new User("5", 4);
        // Add users to lobby
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user1, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user2, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user3, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user4, null));
        assertDoesNotThrow(() -> LobbyHandler.getLobbyHandler().addClient(user5, null));

        Message message = assertDoesNotThrow(() -> Serializer.generateLobbyMessage());

        // Check content of ClientLobby
        ClientLobby clientLobby = assertDoesNotThrow(() -> Serializer.fromMessageToClientLobby(message));
        assertEquals(1, clientLobby.getLobbyStatus().get(ModelConstants.TWO_PLAYERS));
        assertEquals(2, clientLobby.getLobbyStatus().get(ModelConstants.THREE_PLAYERS));
        assertEquals(2, clientLobby.getLobbyStatus().get(ModelConstants.FOUR_PLAYERS));

        LobbyHandler.getLobbyHandler().emptyMap();
    }

    /**
     * Checks if the ClientRound object has been populated with the content of the Round message.
     */

    @Test
    void fromMessageToClientRound() {
        User user1 = new User("kevin", 2);
        User user2 = new User("steph", 2);
        User user3 = new User("ja", 4);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(() -> Serializer.generateRoundMessage(gameEngine));

        // Check content of ClientRound
        ClientRound clientRound = assertDoesNotThrow(() -> Serializer.fromMessageToClientRound(message));
        assertEquals(0, clientRound.getPossibleActions().get(0));
        assertTrue(clientRound.getCurrentPlayer() <= 3 && clientRound.getCurrentPlayer() >= 1);
    }

    /**
     * Checks if the ClientEndGame object has been populated with the content of the End Game message.
     */

    @Test
    void fromMessageToClientEndGame() {
        User user1 = new User("kevin", 2);
        User user2 = new User("steph", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(player1);
        winners.add(player2);

        Message message = assertDoesNotThrow(() -> Serializer.generateEndGameMessage(winners));

        // Check content of ClientEndGame
        ArrayList<String> winnersAfterDeserialization = assertDoesNotThrow(() -> Serializer.fromMessageToClientEndGame(message));
        assertTrue(winnersAfterDeserialization.get(0).equals("kevin"));
        assertTrue(winnersAfterDeserialization.get(1).equals("steph"));
    }

    /**
     * Checks if the conversion from an ActionMessage object to a Message object is correct.
     */
    @Test
    void fromActionMessageToMessage() {
        Map<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_CALCULATE_INFLUENCE_OPTIONS_KEY_ISLAND, "-1");

        // Convert action message to message
        Message message = Serializer.fromActionMessageToMessage(new ActionMessage(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, options));

        // Check if the type and payload of the message are correct
        assertTrue(message.getType().equals(MessageTypes.ACTION));
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(message.getPayload(), JsonObject.class);
        assertTrue(jsonObject.has("actionId"));
        assertTrue(jsonObject.has("options"));
        assertEquals(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, jsonObject.get("actionId").getAsInt());
        Type type = (new TypeToken<Map<String, String>>() {
        }).getType();
        Map<String, String> optionsFromMessage = gson.fromJson(jsonObject.get("options"), type);
        assertTrue(optionsFromMessage.get(ModelConstants.ACTION_CALCULATE_INFLUENCE_OPTIONS_KEY_ISLAND).equals(Integer.toString(-1)));
    }
}