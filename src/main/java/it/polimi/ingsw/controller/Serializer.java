package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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

    public static Message fromStringToMessage(String json) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(json, Message.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The json cannot be converted to a Message object");
        }
    }

    public static String fromMessageToString(Message message) {
        return new Gson().toJson(message);
    }

    public static ActionMessage fromMessageToActionMessage(Message message) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(message.getPayload(), ActionMessage.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to an ActionMessage object");
        }
    }

    public static User fromMessageToUser(Message message) throws WrongMessageContentException {
        try {
            return new Gson().fromJson(message.getPayload(), User.class);
        } catch (JsonSyntaxException e) {
            throw new WrongMessageContentException("The payload of the message cannot be converted to a User object");
        }

    }

    public static String generateGameMessage(GameEngine gameEngine) throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bag.class, new BagSerializer())
                .registerTypeAdapter(MotherNature.class, new MotherNatureSerializer())
                .registerTypeAdapter(CharacterCard.class, new CharacterCardSerializer())
                .registerTypeAdapter(StudentDisc.class, new StudentDiscSerializer())
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .create();
        JsonObject jsonObjectPayload = new JsonObject();
        jsonObjectPayload.add("table", gson.toJsonTree(gameEngine.getTable()));

        ArrayList<Player> players = new ArrayList<>();
        for (Team team : gameEngine.getTeams())
            for (Player player : team.getPlayers())
                players.add(player);

        jsonObjectPayload.add("players", gson.toJsonTree(players));

        JsonObject jsonObjectMessage = new JsonObject();
        jsonObjectMessage.addProperty("type", String.valueOf(MessageTypes.GAME));
        jsonObjectMessage.add("payload", gson.toJsonTree(jsonObjectPayload));
        return gson.toJson(jsonObjectMessage);
    }

    /*
    public static String generateLobbyMessage() {
    }
    */

    public static String generateRoundMessage(GameEngine gameEngine) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Round.class, new RoundSerializer())
                .create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", String.valueOf(MessageTypes.ROUND));
        jsonObject.add("payload", gson.toJsonTree(gameEngine.getRound()));
        return gson.toJson(jsonObject);
    }

    /*
    public static String generateEndGameMessage() {

    }*/
}