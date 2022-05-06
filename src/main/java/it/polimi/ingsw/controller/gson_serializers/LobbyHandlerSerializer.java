package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.controller.LobbyHandler;

import java.lang.reflect.Type;

public class LobbyHandlerSerializer implements JsonSerializer<LobbyHandler> {

    @Override
    public JsonElement serialize(LobbyHandler lobbyHandler, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Integer.toString(ControllerConstants.TWO_PLAYERS_PREFERENCE), lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.TWO_PLAYERS_PREFERENCE)
                        .size());
        jsonObject.addProperty(Integer.toString(ControllerConstants.THREE_PLAYERS_PREFERENCE), lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.THREE_PLAYERS_PREFERENCE)
                        .size());
        jsonObject.addProperty(Integer.toString(ControllerConstants.FOUR_PLAYERS_PREFERENCE),lobbyHandler.getClientsWaiting()
                        .get(ControllerConstants.FOUR_PLAYERS_PREFERENCE)
                        .size());
        return jsonObject;
    }
}