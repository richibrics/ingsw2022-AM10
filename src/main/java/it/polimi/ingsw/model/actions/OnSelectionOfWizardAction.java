package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnSelectionOfWizardAction extends Action {
    private Integer chosenWizardId;

    public OnSelectionOfWizardAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD))
            throw new WrongMessageContentException("ActionMessage doesn't contain the wizard id");
        try {
            this.chosenWizardId = Integer.parseInt(options.get(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Wizard id from the ActionMessage");
        }
    }

    /**
     * Sets the Wizard for the player that asked it
     *
     * @throws Exception if something bad happens
     */
    public void act() throws Exception {
        // Check that nobody has the chosen Wizard
        if (this.getGameEngine().getAssistantManager().isWizardAvailableToBeChosen(this.chosenWizardId))
            this.getGameEngine().getAssistantManager().setWizard(this.getPlayerId(), chosenWizardId);
        else
            throw new IllegalGameActionException("The requested Wizard had already been selected and can't be set to the player");
    }


    /**
     * Modifies the round for the next Players actions.
     * If none of the players have done the selection, set as next action the selection for the next player.
     * Else if everybody did it, pass to the next Action with the same order of play:
     * after this selection, system draws the students from the bag to the cloud.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        if (!this.getGameEngine().getRound().playerTurnEnded()) { // After there's nobody
            // Remove this action from player's actions list
            ArrayList<Integer> nextActions = getGameEngine().getRound().getPossibleActions();
            // Remove, if it returns false, the action isn't inside -> Wrong game state
            if (!nextActions.remove(Integer.valueOf(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID)))
                throw new IllegalGameStateException("OnSelectionOfWizard action was run but it wasn't in Round actions");
            getGameEngine().getRound().setPossibleActions(nextActions);
            // Draw from bag to cloud automatically
            this.getGameEngine().getActionManager().prepareAndExecuteAction(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_ID, ModelConstants.NO_PLAYER, new HashMap<>(), true);
            // Next actions are set from the DrawFromBagToCloud; the order is not set to permit using the same order as before
        }
        // Else, this action remains in the player's actions list (I don't edit round actions)
    }

}
