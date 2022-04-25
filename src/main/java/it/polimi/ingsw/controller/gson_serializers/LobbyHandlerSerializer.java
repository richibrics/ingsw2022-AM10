package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.User;

import java.lang.reflect.Type;
import java.util.Map;

public class LobbyHandlerSerializer implements JsonSerializer<LobbyHandler> {

    private Integer getLastIndex(Map.Entry<Integer, User[]> users) {
        Integer index = 0;
        while (users.getValue()[index] != null)
            index++;
        return index;
    }

    private String userArrayToString(Map.Entry<Integer, User[]> users) {
        return users.getKey().toString() + "-player game: " + this.getLastIndex(users).toString() + "/" + Integer.valueOf(users.getValue().length).toString();
    }

    @Override
    public JsonElement serialize(LobbyHandler lobbyHandler, Type typeOfSrc, JsonSerializationContext context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonObject jsonObject = new JsonObject();
        JsonArray listOfUsers = new JsonArray();


        for (Map.Entry<Integer, User[]> users : lobbyHandler.getUsersWaiting().entrySet()) {
            listOfUsers.add(this.userArrayToString(users));
        }

        jsonObject.add("listOfUsersWaiting", listOfUsers);
        return jsonObject;
    }
}