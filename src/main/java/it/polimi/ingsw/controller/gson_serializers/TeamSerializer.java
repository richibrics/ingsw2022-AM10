package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.ProfessorPawn;

import java.lang.reflect.Type;

public class TeamSerializer implements JsonSerializer<Team>  {
    @Override
    public JsonElement serialize(Team team, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .create();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("towersColor", team.getTeamTowersColor().toString());
        } catch (TowerNotSetException e) {
            jsonObject.addProperty("towersColor", "null");
        }
        jsonObject.addProperty("numberOfTowers", team.getTowers().size());
        JsonArray jsonArray = new JsonArray();
        for (ProfessorPawn professorPawn : team.getProfessorTable()) {
            jsonArray.add(professorPawn.getColor().toString());
        }
        jsonObject.add("professorPawns", gson.toJsonTree(jsonArray));
        jsonArray = new JsonArray();
        for (Player player : team.getPlayers()) {
            jsonArray.add(gson.toJsonTree(player));
        }
        jsonObject.add("players", gson.toJsonTree(jsonArray));
        return jsonObject;
    }
}