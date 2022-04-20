package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.*;

public class OnSelectionOfAssistantsCardAction extends Action {

    static final String OPTIONS_ASSISTANT_ID_KEY = "assistant";
    private Integer chosenAssistantId;

    public OnSelectionOfAssistantsCardAction(GameEngine gameEngine) {
        super(2, gameEngine);
    }

    @Override
    public void setOptions(Map<String,String> options) throws Exception {
        if (!options.containsKey(OPTIONS_ASSISTANT_ID_KEY))
            throw new WrongMessageContentException("ActionMessage doesn't contain the assistant id");
        try {
            this.chosenAssistantId = Integer.parseInt(options.get(OPTIONS_ASSISTANT_ID_KEY));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Assistant id from the ActionMessage");
        }
    }

    @Override
    public void act() throws Exception {
        // TODO moveAssistantCardInHandToLastPlayed should be called before the start of this round, not here
        try {
            // Check legal choose: previous players should not have the same assistant card value unless this
            // player has only that choose
            ArrayList<Integer> playableCards = this.getGameEngine().getAssistantManager().getPlayableAssistantCardValues(this.getPlayerId());
            if(playableCards.contains(this.getGameEngine().getAssistantManager().getCardValueById(this.chosenAssistantId))) {
                // Okay now I can set his assistant card
                this.getGameEngine().getAssistantManager().setAssistantCard(this.getPlayerId(), this.chosenAssistantId);
            }
            else {
                throw new IllegalGameActionException("Requested AssistantCard can't be played");
            }
        } catch (NoSuchElementException e) {
            throw new IllegalGameActionException(e.getMessage());
        }
    }


    /**
     * if not all the players have done the selection, set as next action the selection for the next player.
     * Else if everybody did it, pass to the next Action
     */
    @Override
    void modifyRound() throws Exception {
        if(this.getGameEngine().getRound().playerTurnEnded()) { // There's someone else next
            // Set that the next player has to select the assistant
            ArrayList<Integer> nextActions = new ArrayList<>();
            nextActions.add(this.getId());
            this.getGameEngine().getRound().setPossibleActions(nextActions);
        }
        else {
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
        }
    }
}
