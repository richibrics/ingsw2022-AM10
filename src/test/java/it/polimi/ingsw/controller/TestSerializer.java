package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Wizard;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import org.junit.jupiter.api.Test;

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
        Message targetMessage = assertDoesNotThrow(()->Serializer.fromStringToMessage(string));
        assertEquals(targetMessage.getType(), MessageTypes.TABLE);
        assertEquals(targetMessage.getPayload(), "{\"hello\": \"hi\"}");

        // Check if Serializer.fromStringToMessage throws a WrongMessageContentException

        // Incompatible message
        String wrongString1 = "hello";
        assertThrows(WrongMessageContentException.class, ()->Serializer.fromStringToMessage(wrongString1));

        // Missing json
        String wrongString2 = "";
        assertThrows(WrongMessageContentException.class, ()->Serializer.fromStringToMessage(wrongString2));

        // Wrong fields
        String wrongString3 = "{\"payload\": \"{\\\"hello\\\": \\\"hi\\\"}\"}";
        assertThrows(WrongMessageContentException.class, ()->Serializer.fromStringToMessage(wrongString3));
        String wrongString4 = "{\"type\": \"%s\"}";
        assertThrows(WrongMessageContentException.class, ()->Serializer.fromStringToMessage(wrongString4));
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
        ActionMessage targetActionMessage = assertDoesNotThrow(()-> Serializer.fromMessageToActionMessage(message));
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
        User user = assertDoesNotThrow(()->Serializer.fromMessageToUser(message));
        assertEquals(user.getId(), "kevin");
        assertEquals(user.getPreference(), 3);
    }

    /**
     * Checks that the Table message has the correct type and prints the table message for the developer to read it.
     */
    @Test
    void generateTableMessage() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3,3);
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
        assertDoesNotThrow(()->gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(()-> Serializer.generateTableMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.TABLE, message.getType());
        // Check json printed on stdout
        System.out.println(Serializer.fromMessageToString(message));
    }


    /**
     * Checks that the Teams message has the correct type and prints the table message for the developer to read it.
     */
    @Test
    void generateTeamsMessage() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3,3);
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        assistantCards.add(new AssistantCard(1, 3,2));
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
        assertDoesNotThrow(()->gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(()-> Serializer.generateTeamsMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.TEAMS, message.getType());
        // Check json printed on stdout
        System.out.println(Serializer.fromMessageToString(message));
    }

    /**
     * Checks that the LobbyMessage has the correct type and prints the lobby message for the developer to read it.
     */
    @Test
    void generateLobbyMessage() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 3);
        User user3 = new User("3", 4);
        User user4 = new User("4", 4);
        User user5 = new User("5", 2);
        // Add users to lobby
        assertDoesNotThrow(()->LobbyHandler.getLobbyHandler().addClient(user1, null));
        assertDoesNotThrow(()->LobbyHandler.getLobbyHandler().addClient(user2, null));
        assertDoesNotThrow(()->LobbyHandler.getLobbyHandler().addClient(user3, null));

        Message message = assertDoesNotThrow(()-> Serializer.generateLobbyMessage());
        // Check type
        assertEquals(MessageTypes.LOBBY, message.getType());
        // Check json printed on stdout
        System.out.println(message.getPayload());
    }

    /**
     * Checks that the RoundMessage has the correct type and prints the round message for the developer to read it.
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
        assertDoesNotThrow(()->gameEngine.getActionManager().generateActions());

        Message message = assertDoesNotThrow(()-> Serializer.generateRoundMessage(gameEngine));
        // Check type
        assertEquals(MessageTypes.ROUND, message.getType());
        // Check json in payload printed on stdout
        System.out.println(message.getPayload());
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

        Message message = assertDoesNotThrow(()-> Serializer.generateEndGameMessage(winners));
        // Check type
        assertEquals(MessageTypes.END_GAME, message.getType());
        // Check json in payload
        assertEquals(message.getPayload(), "[\"kevin\",\"steph\"]");
    }
}