package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.LobbyHandler;
import it.polimi.ingsw.controller.User;

import java.lang.reflect.Type;

public class LobbyHandlerSerializer implements JsonSerializer<LobbyHandler> {

    @Override
    public JsonElement serialize(LobbyHandler lobbyHandler, Type typeOfSrc, JsonSerializationContext context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonObject jsonObject = new JsonObject();
        JsonArray listOfUsers = new JsonArray();


        for (User user : lobbyHandler.getUsersWaiting()) {
            JsonElement jsonElement = gsonBuilder.create().toJsonTree(user);
            listOfUsers.add(jsonElement);
        }

        jsonObject.add("listOfUsersWaiting", listOfUsers);
        return jsonObject;
    }
}