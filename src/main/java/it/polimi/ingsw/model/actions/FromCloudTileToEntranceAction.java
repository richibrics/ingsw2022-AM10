package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FromCloudTileToEntranceAction extends Action {

    private int cloudId;

    public FromCloudTileToEntranceAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * @param options additional information for act method
     * @throws Exception if the required key is not provided
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID))
            throw new WrongMessageContentException("A required key is not provided");

        try {
            this.cloudId = Integer.parseInt(options.get(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Cloud id from the ActionMessage");
        }
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        /* Check if the round has ended*/
        if (!this.getGameEngine().getRound().playerTurnEnded()) // Round ended
            this.getGameEngine().getActionManager().prepareAndExecuteAction(ModelConstants.ACTION_CHECK_END_MATCH_CONDITION_ID, -1, new HashMap<>(), true);
        else { // Round not ended: next player's turn
            ArrayList<Integer> listOfActions = new ArrayList<>();
            listOfActions.add(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID);
            listOfActions.add(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID);
            this.getGameEngine().getRound().setPossibleActions(listOfActions);
        }
    }

    /**
     * Moves the students from a cloud tile to the entrance of {@code this.playerId}.
     * @throws Exception if something bad happens
     */

    @Override
    public void act() throws Exception {
        this.getGameEngine().getSchoolPawnManager().moveStudentsFromCloudTileToEntrance(this.getPlayerId(), this.cloudId);
    }
}
