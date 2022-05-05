package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.controller.gson_serializers.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class Serializer {

    private static Gson gson = null;

    /**
     * Returns the Gson object, that is instantiated only the first time it is requested.
     * This Gson works with all the TypeAdapters.
     * @return Gson object
     */
    private static Gson getGson() {
        if (gson == null) {
            // Creates a Gson with all the type adapters
            Type type = (new TypeToken<Map<Integer, CharacterCard>>() {
            }).getType();
            gson = new GsonBuilder()
                    .registerTypeAdapter(Bag.class, new BagSerializer())
                    .registerTypeAdapter(MotherNature.class, new MotherNatureSerializer())
                    .registerTypeAdapter(type, new CharacterMapDeserializer())
                    .registerTypeAdapter(StudentDisc.class, new StudentDiscSerializer())
                    .registerTypeAdapter(IslandTile.class, new IslandTIleSerializer())
                    .registerTypeAdapter(ProfessorPawn.class, new ProfessorPawnSerializer())
                    .registerTypeAdapter(Team.class, new TeamSerializer())
                    .registerTypeAdapter(LobbyHandler.class, new LobbyHandlerSerializer())
                    .registerTypeAdapter(Round.class, new RoundSerializer())
                    .create();
        }
        return gson;
    }

    /**
     * Converts a json string to a Message.
     *
     * @param json the string to convert
     * @return an object of Message
     * @throws WrongMessageContentException if the string cannot be converted to a Message
     * @see Message
     */

    public static Message fromStringToMessage(String json) throws WrongMessageContentException {
        try {
            Message message = getGson().fromJson(json, Message.class);
            // Check message is okay
            if (message == null)
                throw new WrongMessageContentException("The json cannot be converted to a Message object");
            // Check message fields aren't null
            if (message.getType() == null || message.getPayload() == null)
                throw new WrongMessageContentException("Missing message fields");
            else
                return message;
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The json cannot be converted to a Message object");
        }
    }

    /**
     * Converts a message to a json string.
     *
     * @param message the message to convert
     * @return a json string
     * @see Message
     */

    public static String fromMessageToString(Message message) {
        return getGson().toJson(message);
    }

    /**
     * Converts a message to an action message.
     *
     * @param message the message to convert
     * @return an action message
     * @throws WrongMessageContentException if the message cannot be converted to an action message
     * @see Message
     * @see ActionMessage
     */

    public static ActionMessage fromMessageToActionMessage(Message message) throws WrongMessageContentException {
        try {
            return getGson().fromJson(message.getPayload(), ActionMessage.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to an ActionMessage object");
        }
    }

    /**
     * Converts a message to a user.
     *
     * @param message the message to convert
     * @return a user
     * @throws WrongMessageContentException if the message cannot be converted to a user
     * @see Message
     * @see User
     */

    public static User fromMessageToUser(Message message) throws WrongMessageContentException {
        try {
            return getGson().fromJson(message.getPayload(), User.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a User object");
        }

    }

    /**
     * Generates the table message, which contains most of the objects of the match.
     *
     * @param gameEngine the game engine
     * @return a Message object containing the Table
     * @throws Exception if something bad happens during the conversion
     */
    public static Message generateTableMessage(GameEngine gameEngine) throws Exception {
        return new Message(MessageTypes.TABLE, getGson().toJson(gameEngine.getTable()));
    }

    /**
     * Generates the teams message, which contains the teams and the players of the game with their towers and professors.
     *
     * @param gameEngine the game engine
     * @return a Message object containing the Teams
     * @throws Exception if something bad happens during the conversion
     */
    public static Message generateTeamsMessage(GameEngine gameEngine) throws Exception {
        return new Message(MessageTypes.TEAMS, getGson().toJson(gameEngine.getTeams()));
    }

    /**
     * Generates the lobby message, which contains the state of the lobby (users waiting for each game type).
     *
     * @return a Message object containing the state of the lobby
     */
    public static Message generateLobbyMessage() {
        return new Message(MessageTypes.LOBBY, getGson().toJson(LobbyHandler.getLobbyHandler()));
    }


    /**
     * Generates the round message, which contains the state of the round.
     *
     * @param gameEngine the game engine
     * @return a json string containing the state of the round
     */

    public static Message generateRoundMessage(GameEngine gameEngine) {
        return new Message(MessageTypes.ROUND, getGson().toJson(gameEngine.getRound()));
    }


    public static Message generateEndGameMessage(ArrayList<Player> winners) {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player player : winners)
            usernames.add(player.getUsername());
        return new Message(MessageTypes.END_GAME, getGson().toJson(usernames));
    }
}