package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.lang.reflect.Type;

/**
 * Class used to personalize the JSON built from the gson library.
 * For the bag we don't send all the students that are inside it but only the number of elements remaining in the bag.
 */
public class CharacterCardSerializer implements JsonSerializer<CharacterCard> {
    @Override
    public JsonElement serialize(CharacterCard characterCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();

        jsonObject.addProperty("cost", characterCard.getCost());
        JsonArray storageJson = new JsonArray();

        for(StudentDisc studentDisc: characterCard.getStudentsStorage()) {
            storageJson.add(gsonBuilder.create().toJsonTree(studentDisc.getId()));
        }

        jsonObject.add("storage", storageJson);
        return jsonObject;
    }
}
