package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestSerializer {

    @Test
    void fromStringToMessage() {
        // Create string in json format
        String string = String.format("{\"type\": \"%s\", \"payload\": \"{\\\"hello\\\": \\\"hi\\\"}\"}", MessageTypes.GAME);
        // Convert string to message
        Message targetMessage = assertDoesNotThrow(()->Serializer.fromStringToMessage(string));
        assertEquals(targetMessage.getType(), MessageTypes.GAME.toString());
        assertEquals(targetMessage.getPayload(), "{\"hello\": \"hi\"}");
        // Check if Serializer.fromStringToMessage throws a WrongMessageContentException
        String wrongString = "hello";
        assertThrows(WrongMessageContentException.class, ()->Serializer.fromStringToMessage(wrongString));
    }

    @Test
    void fromMessageToString() {
        Message message = new Message(MessageTypes.GAME.toString(), "{\"hello\"}");
        assertEquals(String.format("{\"type\":\"%s\",\"payload\":\"{\\\"hello\\\"}\"}", MessageTypes.GAME), Serializer.fromMessageToString(message));
    }

    @Test
    void fromMessageToActionMessage() {
        // Create action message
        Map<String, String> options = new HashMap<>();
        options.put("color", "red");
        ActionMessage actionMessage = new ActionMessage(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, options);
        // Create message
        Gson gson = new GsonBuilder().create();
        Message message = new Message(MessageTypes.ACTION.toString(), gson.toJson(actionMessage));
        // Convert message to action message with Serializer.fromMessageToActonMessage
        ActionMessage targetActionMessage = assertDoesNotThrow(()-> Serializer.fromMessageToActionMessage(message));
        assertEquals(targetActionMessage.getActionId(), ModelConstants.ACTION_CALCULATE_INFLUENCE_ID);
        assertEquals(targetActionMessage.getOptions().get("color"), "red");
    }

    @Test
    void fromMessageToUser() {
        // Create message
        Message message = new Message(MessageTypes.USER.toString(), "{\"id\": \"kevin\", \"preference\": 3}");
        // Convert message to user with Serializer.fromMessageToUser
        User user = assertDoesNotThrow(()->Serializer.fromMessageToUser(message));
        assertEquals(user.getId(), "kevin");
        assertEquals(user.getPreference(), 3);
    }

    @Test
    void generateGameMessage() {
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
        // Check json printed on stdout
        assertDoesNotThrow(()->System.out.println(Serializer.generateGameMessage(gameEngine)));
    }

    @Test
    void generateLobbyMessage() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 3);
        User user3 = new User("3", 4);
        User user4 = new User("4", 4);
        User user5 = new User("5", 2);
        // Add users to lobby
        LobbyHandler.getLobbyHandler().addClient(user1, new ServerClientConnection(new Socket()));
        LobbyHandler.getLobbyHandler().addClient(user2, new ServerClientConnection(new Socket()));
        LobbyHandler.getLobbyHandler().addClient(user3, new ServerClientConnection(new Socket()));
        LobbyHandler.getLobbyHandler().addClient(user4, new ServerClientConnection(new Socket()));
        LobbyHandler.getLobbyHandler().addClient(user5, new ServerClientConnection(new Socket()));
        // Check json printed on stdout
        assertDoesNotThrow(()->System.out.println(Serializer.generateLobbyMessage()));
    }

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
        // Check json printed on stdout
        assertDoesNotThrow(()->System.out.println(Serializer.generateRoundMessage(gameEngine)));
    }

    @Test
    void generateEndGameMessage() {
        User user1 = new User("kevin", 2);
        User user2 = new User("steph", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(player1);
        winners.add(player2);
        // Check json string returned by Serializer.generateEndGameMessage
        assertEquals(Serializer.generateEndGameMessage(winners), "{\"type\":\"END_GAME\",\"payload\":\"[\\\"kevin\\\",\\\"steph\\\"]\"}");
    }
}