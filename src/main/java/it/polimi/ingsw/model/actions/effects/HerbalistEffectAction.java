package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

public class HerbalistEffectAction extends Action {
    private Integer chosenIslandId;

    public HerbalistEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_HERBALIST_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get the island id from the options.
     *
     * @param options additional information for act method
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND))
            throw new WrongMessageContentException("ActionMessage doesn't contain the island id");
        try {
            this.chosenIslandId = Integer.parseInt(options.get(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Island id from the ActionMessage");
        }

        if (this.chosenIslandId < 1 || this.chosenIslandId > ModelConstants.ISLAND_TILES_NUMBER)
            throw new WrongMessageContentException("Island id not in [1,12]");
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
    }

    @Override
    public void act() throws Exception {
        // Check I have available no entry tiles
        if (this.getGameEngine().getTable().getAvailableNoEntryTiles() <= 0)
            throw new IllegalGameActionException("Zero no entry tiles available");

        // Check island hasn't got entry tiles
        if (CommonManager.takeIslandTileById(this.getGameEngine(), this.chosenIslandId).hasNoEntry())
            throw new IllegalGameActionException("Island group already has the no entry tile");

        // Now I can set the attribute to the entire group
        this.getGameEngine().getIslandManager().setIslandGroupNoEntryByIslandId(this.chosenIslandId, true);

        // Decrease available No Entry Tiles
        this.getGameEngine().getTable().decreaseAvailableNoEntryTiles();
    }
}
