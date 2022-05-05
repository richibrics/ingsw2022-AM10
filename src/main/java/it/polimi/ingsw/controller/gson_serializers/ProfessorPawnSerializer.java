package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.model.game_components.ProfessorPawn;

import java.lang.reflect.Type;

public class ProfessorPawnSerializer implements JsonSerializer<ProfessorPawn> {
    @Override
    public JsonElement serialize(ProfessorPawn professorPawn, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(professorPawn.getColor().toString());
    }
}
