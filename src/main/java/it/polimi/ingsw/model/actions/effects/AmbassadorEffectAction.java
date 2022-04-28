package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;

import java.util.HashMap;
import java.util.Map;

public class AmbassadorEffectAction extends Action {

    private String islandId;

    public AmbassadorEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_AMBASSADOR_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get the island id from the options.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND))
            throw new WrongMessageContentException("ActionMessage doesn't contain the key " + ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND);
        else
            this.islandId = options.get(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND);
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player.
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens.
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
    }

    /**
     * Calculates the influence on the island specified in options.
     *
     * @throws Exception if something bad happens.
     */

    @Override
    public void act() throws Exception {
        Map<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND, this.islandId);
        this.getGameEngine().getActionManager().executeAction(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, -1, options);
    }
}