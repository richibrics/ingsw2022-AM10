package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.controller.LobbyHandler;

import java.lang.reflect.Type;

public class LobbyHandlerSerializer implements JsonSerializer<LobbyHandler> {

    @Override
    public JsonElement serialize(LobbyHandler lobbyHandler, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(String.format("%d-player game", ControllerConstants.TWO_PLAYERS_PREFERENCE),
                String.format("%d/%d", lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.TWO_PLAYERS_PREFERENCE)
                        .size(), ControllerConstants.TWO_PLAYERS_PREFERENCE));
        jsonArray.add(jsonObject1);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty(String.format("%d-player game", ControllerConstants.THREE_PLAYERS_PREFERENCE),
                String.format("%d/%d", lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.THREE_PLAYERS_PREFERENCE)
                        .size(), ControllerConstants.THREE_PLAYERS_PREFERENCE));
        jsonArray.add(jsonObject2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty(String.format("%d-player game", ControllerConstants.FOUR_PLAYERS_PREFERENCE),
                String.format("%d/%d", lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.FOUR_PLAYERS_PREFERENCE)
                        .size(), ControllerConstants.FOUR_PLAYERS_PREFERENCE));
        jsonArray.add(jsonObject3);
        jsonObject.addProperty("listOfUsersWaiting", gson.toJson(jsonArray));
        return jsonObject;
    }
}