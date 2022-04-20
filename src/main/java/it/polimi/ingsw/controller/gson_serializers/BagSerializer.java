package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.MotherNature;

import java.lang.reflect.Type;

/**
 * Class used to personalize the JSON built from the gson library.
 * For the bag we don't send all the students that are inside it but only the number of elements remaining in the bag.
 */
public class BagSerializer implements JsonSerializer<Bag> {
    @Override
    public JsonElement serialize(Bag bag, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("students", bag.getNumberOfStudents());
        return jsonObject;
    }
}
