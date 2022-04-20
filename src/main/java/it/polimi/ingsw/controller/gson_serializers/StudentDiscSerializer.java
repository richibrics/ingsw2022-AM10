package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.game_components.MotherNature;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.lang.reflect.Type;

/**
 * Class used to personalize the JSON built from the gson library.
 * For the StudentDisc we only need to send its id because the client knows how to retrieve the student color from the id.
 */
public class StudentDiscSerializer implements JsonSerializer<StudentDisc> {
    @Override
    public JsonElement serialize(StudentDisc studentDisc, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(studentDisc.getId());
    }
}
