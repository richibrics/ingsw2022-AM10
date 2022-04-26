package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;

import java.util.Map;

public class AmbassadorEffectAction extends Action {
    private Integer islandId;
    public AmbassadorEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_AMBASSADOR_ID, gameEngine);
    }


    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND))
            throw new WrongMessageContentException("ActionMessage doesn't contain the island id");
        try {
            this.islandId = Integer.parseInt(options.get(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Island id from the ActionMessage");
        }

        if (this.islandId < 1 || this.islandId > ModelConstants.ISLAND_TILES_NUMBER)
            throw new WrongMessageContentException("Island id not in [1,12]");
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {
    }
}
