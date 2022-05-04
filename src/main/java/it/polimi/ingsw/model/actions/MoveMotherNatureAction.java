package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

import java.util.ArrayList;
import java.util.Map;

public class MoveMotherNatureAction extends Action {

    private Integer chosenIslandId;

    public MoveMotherNatureAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get the id of the island where the player wants to place mother nature.
     *
     * @param options additional information for act method
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND))
            throw new WrongMessageContentException("ActionMessage doesn't contain the island id");
        try {
            this.chosenIslandId = Integer.parseInt(options.get(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing island id from the ActionMessage");
        }

        if (this.chosenIslandId < 1 || this.chosenIslandId > ModelConstants.NUMBER_OF_ISLAND_TILES)
            throw new WrongMessageContentException("Island id received is not in [1,12]");

    }

    /**
     * Moves mother nature to the requested Island
     *
     * @throws Exception if something bad happens, like cannot move to that island (IllegalGameActionException) or the items
     *                   are not correctly set in the game (IllegalGameStateException)
     */

    @Override
    public void act() throws Exception {
        try {
            this.getGameEngine().getIslandManager().moveMotherNature(this.getPlayerId(), this.chosenIslandId);
        } catch (IllegalArgumentException e) {
            throw new IllegalGameActionException(e.getMessage());
        } catch (TableNotSetException | AssistantCardNotSetException e) {
            throw new IllegalGameStateException(e.getMessage());
        }
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * Here I remove this action from the round and calls directly the calculate influence.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        ArrayList<Integer> nextActions = this.getGameEngine().getRound().getPossibleActions();
        if (!nextActions.remove(Integer.valueOf(this.getId())))
            throw new IllegalGameStateException("MoveMotherNature action was run but it wasn't in Round actions");
        this.getGameEngine().getRound().setPossibleActions(nextActions);

        // Run calculate influence, with modifyRoundAndAction flag ON
        this.getGameEngine().getActionManager().executeInternalAction(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, this.getPlayerId(), true);
    }
}
