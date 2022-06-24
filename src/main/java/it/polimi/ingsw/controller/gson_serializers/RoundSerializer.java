package it.polimi.ingsw.controller.gson_serializers;

import com.google.gson.*;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.exceptions.PlayerOrderNotSetException;

import java.lang.reflect.Type;

/**
 * Serializer for the Round class. The properties needed are the current player and the list of the actions the current
 * player can perform.
 */

public class RoundSerializer implements JsonSerializer<Round> {

    @Override
    public JsonElement serialize(Round round, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray listOfActions = new JsonArray();
        for (Integer actionId : round.getPossibleActions())
            listOfActions.add(actionId);

        jsonObject.add("listOfActions", listOfActions);
        try {
            jsonObject.addProperty("currentPlayer", round.getCurrentPlayer());
        }

        catch (PlayerOrderNotSetException e) {
            jsonObject.addProperty("currentPlayer", ControllerConstants.ID_WHEN_PAYER_NOT_SET);
        }

        return jsonObject;
    }
}