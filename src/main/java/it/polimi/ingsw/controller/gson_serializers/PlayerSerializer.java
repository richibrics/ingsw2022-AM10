package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.Player;
import java.lang.reflect.Type;

/**
 * Serializer for the Player class. The properties needed are the username of the user associated to
 * the player and the amount of coins owned by the player.
 */

public class PlayerSerializer implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", player.getUsername());
        jsonObject.addProperty("coins", player.getCoins());
        return jsonObject;
    }
}