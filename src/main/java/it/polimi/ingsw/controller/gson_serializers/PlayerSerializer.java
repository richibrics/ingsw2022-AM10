package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.WizardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Serializer for the Player class. The properties needed are the username of the user associated to
 * the player, the amount of coins owned by the player, the player's wizard and the player's assistant cards.
 */

public class PlayerSerializer implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", player.getUsername());
        jsonObject.addProperty("playerId", player.getPlayerId());
        jsonObject.addProperty("coins", player.getCoins());
        try {
            jsonObject.addProperty("wizard", player.getWizard().getId());
        } catch (WizardNotSetException e) {
            jsonObject.addProperty("wizard", -1);
        }
        JsonArray jsonArray = new JsonArray();
        try {
            for (AssistantCard assistantCard : player.getWizard().getAssistantCards())
                jsonArray.add(gson.toJsonTree(assistantCard));
            jsonObject.add("assistantCards", jsonArray);
        } catch (WizardNotSetException e) {
            jsonObject.add("assistantCards", gson.toJsonTree(new ArrayList<>()));
        }
        return jsonObject;
    }
}