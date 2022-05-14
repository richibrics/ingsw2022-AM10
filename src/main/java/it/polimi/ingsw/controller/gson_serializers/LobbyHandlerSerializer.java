package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.controller.LobbyHandler;

import java.lang.reflect.Type;

public class LobbyHandlerSerializer implements JsonSerializer<LobbyHandler> {

    @Override
    public JsonElement serialize(LobbyHandler lobbyHandler, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        // Send the two player lobby if the map is available, else send it as empty
        if (lobbyHandler.getClientsWaiting()
                .get(ControllerConstants.TWO_PLAYERS_PREFERENCE) != null) {
            jsonObject.addProperty(Integer.toString(ControllerConstants.TWO_PLAYERS_PREFERENCE), lobbyHandler.getClientsWaiting()
                    .get(ControllerConstants.TWO_PLAYERS_PREFERENCE)
                    .size());
        } else {
            jsonObject.addProperty(Integer.toString(ControllerConstants.TWO_PLAYERS_PREFERENCE), 0);
        }

        if (lobbyHandler.getClientsWaiting()
                .get(ControllerConstants.THREE_PLAYERS_PREFERENCE) != null) {
            jsonObject.addProperty(Integer.toString(ControllerConstants.THREE_PLAYERS_PREFERENCE), lobbyHandler.getClientsWaiting()
                    .get(ControllerConstants.THREE_PLAYERS_PREFERENCE)
                    .size());
        } else {
            jsonObject.addProperty(Integer.toString(ControllerConstants.THREE_PLAYERS_PREFERENCE), 0);
        }

        if (lobbyHandler.getClientsWaiting()
                .get(ControllerConstants.FOUR_PLAYERS_PREFERENCE) != null) {
            jsonObject.addProperty(Integer.toString(ControllerConstants.FOUR_PLAYERS_PREFERENCE), lobbyHandler.getClientsWaiting()
                    .get(ControllerConstants.FOUR_PLAYERS_PREFERENCE)
                    .size());
        } else {
            jsonObject.addProperty(Integer.toString(ControllerConstants.FOUR_PLAYERS_PREFERENCE), 0);
        }
        return jsonObject;
    }
}