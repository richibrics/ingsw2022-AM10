package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game_components.MotherNature;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Class used to personalize the JSON built from the gson library.
 * For mother nature in the json we only need the id of the island
 * mother nature is on, because all the information about the island are
 * already available in the list of island tiles directly in the table.
 */

public class MotherNatureSerializer implements JsonSerializer<MotherNature> {
    @Override
    public JsonElement serialize(MotherNature motherNature, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("island", motherNature.getIslandTile().getId());
        return jsonObject;
    }
}
