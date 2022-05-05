package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.lang.reflect.Type;

public class IslandTIleSerializer implements JsonSerializer<IslandTile> {
    @Override
    public JsonElement serialize(IslandTile islandTile, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StudentDisc.class, new StudentDiscSerializer())
                .create();
        JsonObject islandJson = gson.toJsonTree(islandTile).getAsJsonObject();
        islandJson.remove("tower");
        try {
            islandJson.addProperty("tower", islandTile.getTower().getColor().toString());
        } catch (TowerNotSetException e) {
            islandJson.addProperty("tower", ControllerConstants.NO_TOWER_ON_ISLAND_STRING);
        }
        return islandJson;
    }
}
