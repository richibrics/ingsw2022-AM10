package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;

import java.util.*;

public class OnSelectionOfWizardAction extends Action {
    private Integer chosenWizardId;

    public OnSelectionOfWizardAction(GameEngine gameEngine) {
        super(0, gameEngine);
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
     * @throws Exception if something bad happens
     */
    public void act() throws Exception {
        // Check that nobody has the chosen Wizard
        if(this.getGameEngine().getAssistantManager().isWizardAvailableToBeChosen(this.chosenWizardId))
            this.getGameEngine().getAssistantManager().setWizard(this.getPlayerId(), chosenWizardId);
        else
            throw new IllegalGameActionException("The requested Wizard had already been selected and can't be set to the player");
    }


    /**
     * Modifies the round for the next Players actions.
     * If none of the players have done the selection, set as next action the selection for the next player.
     * Else if everybody did it, pass to the next Action.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        if (this.getGameEngine().getRound().playerTurnEnded()) { // There's someone else next
            // Set that the next player has to select the assistant
            ArrayList<Integer> nextActions = new ArrayList<>();
            nextActions.add(this.getId());
            this.getGameEngine().getRound().setPossibleActions(nextActions);
        } else {
            // Everyone selected the Wizard. Now players have to select the assistant card: I let them play with the same
            // order as here
            this.getGameEngine().getRound().setOrderOfPlay(this.getGameEngine().getRound().getOrderOfPlay());

            // Set next action (select assistant card)
            ArrayList<Integer> nextActions = new ArrayList<>();
            nextActions.add(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID);
            this.getGameEngine().getRound().setPossibleActions(nextActions);
        }
    }

}
