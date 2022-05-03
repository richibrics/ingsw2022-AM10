package it.polimi.ingsw.controller;

import com.google.gson.*;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.controller.gson_serializers.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.MotherNature;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

public class Serializer {

    /**
     * Converts a json string to a Message.
     * @param json the string to convert
     * @return an object of Message
     * @throws WrongMessageContentException if the string cannot be converted to a Message
     * @see Message
     */

    public static Message fromStringToMessage(String json) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(json, Message.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The json cannot be converted to a Message object");
        }
    }

    /**
     * Converts a message to a json string.
     * @param message the message to convert
     * @return a json string
     * @see Message
     */

    public static String fromMessageToString(Message message) {
        return new Gson().toJson(message);
    }

    /**
     * Converts a message to an action message.
     * @param message the message to convert
     * @return an action message
     * @throws WrongMessageContentException if the message cannot be converted to an action message
     * @see Message
     * @see ActionMessage
     */

    public static ActionMessage fromMessageToActionMessage(Message message) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(message.getPayload(), ActionMessage.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to an ActionMessage object");
        }
    }

    /**
     * Converts a message to a user.
     * @param message the message to convert
     * @return a user
     * @throws WrongMessageContentException if the message cannot be converted to a user
     * @see Message
     * @see User
     */

    public static User fromMessageToUser(Message message) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(message.getPayload(), User.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a User object");
        }

    }

    /**
     * Generates the game message, which contains the state of the game (table and players in game).
     * @param gameEngine the game engine
     * @return a json string containing the state of the game
     * @throws Exception if something bad happens during the conversion
     */

    public static String generateGameMessage(GameEngine gameEngine) throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bag.class, new BagSerializer())
                .registerTypeAdapter(MotherNature.class, new MotherNatureSerializer())
                .registerTypeAdapter(CharacterCard.class, new CharacterCardSerializer())
                .registerTypeAdapter(StudentDisc.class, new StudentDiscSerializer())
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .create();
        JsonObject jsonObjectPayload = new JsonObject();
        jsonObjectPayload.addProperty("table", gson.toJson(gameEngine.getTable()));

        ArrayList<Player> players = new ArrayList<>();
        for (Team team : gameEngine.getTeams())
            for (Player player : team.getPlayers())
                players.add(player);

        jsonObjectPayload.addProperty("players", gson.toJson(players));

        JsonObject jsonObjectMessage = new JsonObject();
        jsonObjectMessage.addProperty("type", MessageTypes.GAME.toString());
        jsonObjectMessage.addProperty("payload", gson.toJson(jsonObjectPayload));
        return gson.toJson(jsonObjectMessage);
    }

    /**
     * Generates the lobby message, which contains the state of the lobby (users waiting for each game type).
     * @return a json string containing the state of the lobby
     */

    public static String generateLobbyMessage() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LobbyHandler.class, new LobbyHandlerSerializer())
                .create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", MessageTypes.LOBBY.toString());
        jsonObject.addProperty("payload", gson.toJson(LobbyHandler.getLobbyHandler()));
        return gson.toJson(jsonObject);
    }


    /**
     * Generates the round message, which contains the state of the round.
     * @param gameEngine the game engine
     * @return a json string containing the state of the round
     */

    public static String generateRoundMessage(GameEngine gameEngine) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Round.class, new RoundSerializer())
                .create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", MessageTypes.ROUND.toString());
        jsonObject.addProperty("payload", gson.toJson(gameEngine.getRound()));
        return gson.toJson(jsonObject);
    }


    public static String generateEndGameMessage(ArrayList<Player> winners) {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player player : winners)
            usernames.add(player.getUsername());
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", MessageTypes.END_GAME.toString());
        jsonObject.addProperty("payload", gson.toJson(usernames));
        return gson.toJson(jsonObject);
    }
}