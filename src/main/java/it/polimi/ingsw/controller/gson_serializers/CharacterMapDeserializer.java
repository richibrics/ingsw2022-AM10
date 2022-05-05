package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Class used to serialize the map of character card. It creates an array of json objects with
 * id, cost and storage of the character card.
 */

public class CharacterMapDeserializer implements JsonSerializer<Map<Integer, CharacterCard>> {


    @Override
    public JsonElement serialize(Map<Integer, CharacterCard> src, Type typeOfSrc, JsonSerializationContext context) {

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        for (Map.Entry<Integer, CharacterCard> entry : src.entrySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", entry.getKey());
            jsonObject.addProperty("cost", entry.getValue().getCost());
            // Create json array with students in storage
            JsonArray storageJson = new JsonArray();
            for (StudentDisc studentDisc : entry.getValue().getStudentsStorage()) {
                storageJson.add(gson.toJson(studentDisc.getId()));
            }
            jsonObject.add("storage", storageJson);
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }
}