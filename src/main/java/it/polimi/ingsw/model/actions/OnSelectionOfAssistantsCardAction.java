package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class OnSelectionOfAssistantsCardAction extends Action {
    private Integer chosenAssistantId;

    public OnSelectionOfAssistantsCardAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     *
     * @param options additional information for act method
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_OPTIONS_KEY_ASSISTANT))
            throw new WrongMessageContentException("ActionMessage doesn't contain the assistant id");
        try {
            this.chosenAssistantId = Integer.parseInt(options.get(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_OPTIONS_KEY_ASSISTANT));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Assistant id from the ActionMessage");
        }
    }

    /**
     * Sets the player card if that card is available.
     *
     * @throws Exception if something bad happens
     **/
    @Override
    public void act() throws Exception {
        // moveAssistantCardInHandToLastPlayed that sets player's card to null is set in CheckEndMatchCondition action.
        try {
            // Check legal choose: previous players should not have the same assistant card value unless this
            // player has only that choose
            ArrayList<Integer> playableCards = this.getGameEngine().getAssistantManager().getPlayableAssistantCardValues(this.getPlayerId());
            if (playableCards.contains(this.getGameEngine().getAssistantManager().getCardValueById(this.chosenAssistantId))) {
                // Okay now I can set his assistant card
                this.getGameEngine().getAssistantManager().setAssistantCard(this.getPlayerId(), this.chosenAssistantId);
            } else {
                throw new IllegalGameActionException("Requested AssistantCard can't be played");
            }
        } catch (NoSuchElementException e) {
            throw new IllegalGameActionException(e.getMessage());
        }
    }

    /**
     * Modifies the round for the next Players actions.
     * If none of the players have done the selection, set as next action the selection for the next player.
     * Else if everybody did it, pass to the next Action.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {
        if (!this.getGameEngine().getRound().playerTurnEnded()) { // There's nobody after me
            // Everyone selected the AssistantCard, so now I order the characters turns using the assistant card value.
            ArrayList<Integer> oldOrderOfPlay = this.getGameEngine().getRound().getOrderOfPlay();
            Map<Integer, Float> orderMap = new HashMap<>();
            ArrayList<Integer> newOrderOfPlay = new ArrayList<>();
            // I can't only order by card value: if the card values is the same I have to keep the same order for the players
            // with the same card value.
            // To keep track of the card value and current order I use
            // a map: key = playerId; value = (float) cardValue,positionOfThePlayerInTheCurrentOrder
            for (int i = 1; i <= this.getGameEngine().getNumberOfPlayers(); i++) {
                try {
                    orderMap.put(i, (float) (this.getGameEngine().getAssistantManager().getValueOfAssistantCardInHand(i) + 0.1 * oldOrderOfPlay.indexOf(i)));
                } catch (AssistantCardNotSetException e) {
                    // Can't get here: I get past the else statement only when everyone has the assistant card
                    throw new IllegalGameStateException("AssistantCard not set for a player while calculating new players order");
                }
            }
            // order by value and add into the new order
            orderMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(tuple -> newOrderOfPlay.add(tuple.getKey()));
            this.getGameEngine().getRound().setOrderOfPlay(newOrderOfPlay);

            // Set next actions for the first player of the round
            ArrayList<Integer> nextActions = getGameEngine().getRound().getPossibleActions();
            // Check it was inside: if not, wrong game state
            if (!nextActions.contains(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID))
                throw new IllegalGameStateException("OnSelectionOfAssistantsCard action was run but it wasn't in Round actions");

            nextActions.remove(Integer.valueOf(this.getId()));
            // No more assistant selection

            // TODO non aggiungere ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID se expertMode
            nextActions.add(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID);
            nextActions.add(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID);
            this.getGameEngine().getRound().setPossibleActions(nextActions);
        }
        // Else the actions remains like before, with this action in the round actions list
    }
}
