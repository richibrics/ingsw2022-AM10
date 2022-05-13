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
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.game_objects.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class Serializer {

    private static Gson gson = null;

    /**
     * Returns the Gson object, that is instantiated only the first time it is requested.
     * This Gson works with all the TypeAdapters.
     *
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
     * @see Message
     * @see Table
     */

    public static Message generateTableMessage(GameEngine gameEngine) throws TableNotSetException {
        return new Message(MessageTypes.TABLE, getGson().toJson(gameEngine.getTable()));
    }

    /**
     * Generates the teams message, which contains the teams and the players of the game with their towers and professors.
     *
     * @param gameEngine the game engine
     * @return a Message object containing the Teams
     * @throws Exception if something bad happens during the conversion
     * @see Message
     * @see Team
     */

    public static Message generateTeamsMessage(GameEngine gameEngine) {
        return new Message(MessageTypes.TEAMS, getGson().toJson(gameEngine.getTeams()));
    }

    /**
     * Generates the lobby message, which contains the state of the lobby (users waiting for each game type).
     *
     * @return a Message object containing the state of the lobby
     * @see Message
     * @see LobbyHandler
     */

    public static Message generateLobbyMessage() {
        return new Message(MessageTypes.LOBBY, getGson().toJson(LobbyHandler.getLobbyHandler()));
    }


    /**
     * Generates the round message, which contains the state of the round.
     *
     * @param gameEngine the game engine
     * @return a json string containing the state of the round
     * @see Message
     * @see Round
     */

    public static Message generateRoundMessage(GameEngine gameEngine) {
        return new Message(MessageTypes.ROUND, getGson().toJson(gameEngine.getRound()));
    }

    /**
     * Generates an end game message, which contains the usernames of the winners.
     *
     * @param winners the list of the usernames of the winners
     * @return a Message object containing the winners
     * @see Message
     */

    public static Message generateEndGameMessage(ArrayList<Player> winners) {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player player : winners)
            usernames.add(player.getUsername());
        return new Message(MessageTypes.END_GAME, getGson().toJson(usernames));
    }

    /**
     * Generates a Message object representing a user.
     *
     * @param user the user
     * @return a Message object
     * @see Message
     * @see User
     */

    public static Message fromUserToMessage(User user) {
        return new Message(MessageTypes.USER, getGson().toJson(user));
    }

    /**
     * Generates a ClientTable object from a Message object.
     *
     * @param message the Message object to deserialize
     * @return a ClientTable object
     * @throws WrongMessageContentException if the payload of the message cannot be converted to a ClientTable object
     * @see Message
     * @see ClientTable
     */

    public static ClientTable fromMessageToClientTable(Message message) throws WrongMessageContentException {
        try {
            return getGson().fromJson(message.getPayload(), ClientTable.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a ClientTable object");
        }
    }

    /**
     * Generates a ClientTeams object from a Message object.
     *
     * @param message the Message object to deserialize
     * @return a ClientTeams object
     * @throws WrongMessageContentException if the payload of the message cannot be converted to a ClientTeams object
     * @see Message
     * @see ClientTeams
     */

    public static ClientTeams fromMessageToClientTeams(Message message) throws WrongMessageContentException {
        try {
            Type type = (new TypeToken<ArrayList<ClientTeam>>() {
            }).getType();
            return new ClientTeams(getGson().fromJson(message.getPayload(), type));
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a ClientTeams object");
        }
    }

    /**
     * Generates a ClientLobby object from a Message object.
     *
     * @param message the Message object to deserialize
     * @return a ClientLobby object
     * @throws WrongMessageContentException if the payload of the message cannot be converted to a ClientLobby object
     * @see Message
     * @see ClientLobby
     */

    public static ClientLobby fromMessageToClientLobby(Message message) throws WrongMessageContentException {
        try {
            Type type = (new TypeToken<Map<Integer, Integer>>() {
            }).getType();
            return new ClientLobby(getGson().fromJson(message.getPayload(), type));
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a ClientLobby object");
        }
    }

    /**
     * Generates a ClientRound object from a Message object.
     *
     * @param message the Message object to deserialize
     * @return a ClientRound object
     * @throws WrongMessageContentException if the payload of the message cannot be converted to a ClientRound object
     * @see Message
     * @see ClientRound
     */

    public static ClientRound fromMessageToClientRound(Message message) throws WrongMessageContentException {
        try {
            return getGson().fromJson(message.getPayload(), ClientRound.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a ClientRound object");
        }
    }

    /**
     * Generates a list of usernames of the winners from a Message object.
     *
     * @param message the Message object to deserialize
     * @return the list of usernames of the winners
     * @throws WrongMessageContentException if the payload of the message cannot be converted to a list of usernames
     * @see Message
     */

    public static ArrayList<String> fromMessageToClientEndGame(Message message) throws WrongMessageContentException {
        try {
            Type type = (new TypeToken<ArrayList<String>>() {
            }).getType();
            return getGson().fromJson(message.getPayload(), type);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a list of usernames");
        }
    }

    /**
     * Generates a Message object from an ActionMessage object.
     * @param message the ActionMessage object to convert
     * @return the Message object
     */

    public static Message fromActionMessageToMessage (ActionMessage message) {
        return new Message(MessageTypes.ACTION, getGson().toJson(message));
    }
}