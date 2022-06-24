package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.WizardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Wizard;


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AssistantManager extends Manager {

    public AssistantManager(GameEngine gameEngine) { super(gameEngine); }

    /**
     * Creates all the assistant cards for the wizard with {@code wizardId}.
     * @param wizardId the identifier of the wizard
     * @return the assistant cards for the wizard with {@code wizardId}
     * @see AssistantCard
     * @see Wizard
     */

    private ArrayList<AssistantCard> createAssistantCards (int wizardId) {
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD; i <= ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD;
             i += ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS) {
            assistantCards.add(new AssistantCard(wizardId == 1 ? wizardId * i : ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD
                    * (wizardId - 1) + i, i, i % 2 == 0 ? i / 2 : i / 2 + 1));
        }
        return assistantCards;
    }

    /**
     * Creates a new wizard with {@code wizardId} for the player with {@code playerId}. Throws a NoSuchElementException if the
     * player could not be found.
     * @param playerId the identifier of the player
     * @param wizardId the identifier of the wizard
     * @throws NoSuchElementException if the player could not be found
     */

    public void setWizard(int playerId, int wizardId) throws NoSuchElementException {
        Wizard wizard = new Wizard(wizardId, this.createAssistantCards(wizardId));
        CommonManager.takePlayerById(this.getGameEngine(), playerId).setWizard(wizard);
    }

    /**
     * Sets the assistant card with {@code assistantId} as the current assistant card for the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @param assistantId the identifier of the assistant card
     * @throws NoSuchElementException if the player or the assistant card could not be found
     */

    public void setAssistantCard(int playerId, int assistantId) throws NoSuchElementException {
        CommonManager.takePlayerById(this.getGameEngine(), playerId).setActiveAssistantCard(assistantId);
    }

    /**
     * Gets the value of the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @return the value of the current assistant card of the player
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getValueOfAssistantCardInHand (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return CommonManager.takePlayerById(this.getGameEngine(), playerId).getActiveAssistantCard().getCardValue();
    }

    /**
     * Gets the movements of the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @return the number of movements of mother nature allowed by the card
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getMovementsOfAssistantCardInHand (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return CommonManager.takePlayerById(this.getGameEngine(), playerId).getActiveAssistantCard().getMovements();
    }

    /**
     * Gets the value of the assistant card played by the player with {@code playerId} in the previous round.
     * @param playerId the identifier of the player
     * @return the value of the assistant card played by the player with {@code playerId} in the previous round
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getValueOfLastPlayedAssistantCard (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return CommonManager.takePlayerById(this.getGameEngine(), playerId).getLastPlayedAssistantCard().getCardValue();
    }

    /**
     * Increments by {@code extraMovements} the movements allowed by the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @param extraMovements the extra movements allowed
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public void incrementMovementsOfAssistantCardInHand (int playerId, int extraMovements) throws NoSuchElementException, AssistantCardNotSetException {
        CommonManager.takePlayerById(this.getGameEngine(), playerId).getActiveAssistantCard().incrementMovements(extraMovements);
    }

    /**
     * Pops the current assistant card of the player with {@code playerId} and makes that card the last played assistant card
     * of the same player.
     * @param playerId the identifier of the player
     * @throws NoSuchElementException if the player could not be found
     */

    public void moveAssistantCardInHandToLastPlayed (int playerId) throws NoSuchElementException {
        CommonManager.takePlayerById(this.getGameEngine(), playerId).setLastPlayedAssistantCard(CommonManager.takePlayerById(this.getGameEngine(), playerId).popActiveAssistantCard());
    }

    /**
     * Takes the id of the assistant card and retrieves the card value (useful to check if the card is selectable)
     * @param assistantCardId the id of the assistant card
     * @return the value of the assistant card
     */
    public int getCardValueById(int assistantCardId) {
        return ((assistantCardId-1)%10)+1;
    }

    /**
     * Returns a list of card values that the player can play in this round, considering the cards already played by the
     * other players.
     * MANDATORY: to work it's necessary that the players that in this round haven't already chosen an assistant card, have
     * it as null (this player too) - using moveAssistantCardInHandToLastPlayed it's okay.
     * @param playerId the id of the player
     * @return a list with the values of the assistant cards that can be played by {@code playerId}
     * @throws WizardNotSetException if the player hasn't set a wizard yet
     * @throws NoSuchElementException if the player could not be found
     */
    public ArrayList<Integer> getPlayableAssistantCardValues (int playerId) throws WizardNotSetException, NoSuchElementException {
        // Look at my cards and at the active cards of the other players: if all my cards are active in the other players,
        // I can return all my cards because the player can play all his cards; otherwise I will return only the cards
        // that aren't active in the other players' hands

        // See if all the player's cards are active on the table (this means I can play them indistinctly)
        boolean playerHasOnlyActiveCards = true;
        for (AssistantCard assistantCard: CommonManager.takePlayerById(this.getGameEngine(), playerId).getWizard().getAssistantCards()) {
            if(this.isAssistantCardValueActiveOnTable(assistantCard.getCardValue()) == false) // My card is not active for any other player
                playerHasOnlyActiveCards = false; // then I have at least one card not used from somebody else I can play
        }

        if(playerHasOnlyActiveCards) // returns all the cards because them are all used in the current round from the other players
            return (ArrayList<Integer>) CommonManager.takePlayerById(this.getGameEngine(), playerId).getWizard().getAssistantCards().stream().map((assistantCard)->assistantCard.getCardValue()).collect(Collectors.toList());
        else // returns only the cards that aren't on the table in this turn
            return (ArrayList<Integer>) CommonManager.takePlayerById(this.getGameEngine(), playerId).getWizard().getAssistantCards().stream().map((assistantCard)->assistantCard.getCardValue()).filter((cardValue)->!isAssistantCardValueActiveOnTable(cardValue)).collect(Collectors.toList());
    }

    /**
     * Returns true if there's a card on the table with {@code cardValue}; false otherwise
     * @param cardValue the value of the card to check
     * @return true if there's a card on the table with {@code cardValue}; false otherwise
     */
    private boolean isAssistantCardValueActiveOnTable (int cardValue) {
        for (int i = 1; i <= this.getGameEngine().getNumberOfPlayers(); i++) {
            try {
                if (CommonManager.takePlayerById(this.getGameEngine(), i).getActiveAssistantCard().getCardValue() == cardValue) {
                    return true;
                }
            } catch (AssistantCardNotSetException e) {
                // I am here because the player I'm iterating on hasn't a card, good
            }
        }
        return false;
    }

    /**
     * @param wizardId the id of the wizard to test
     * @return true if {@code wizardId} hasn't been chosen by anyone and is still selectable
     * @throws WizardNotSetException this exception can't be thrown because hasWizard already checks for the wizard presence
     */
    public boolean isWizardAvailableToBeChosen(int wizardId) throws WizardNotSetException {
        for (Team team: this.getGameEngine().getTeams())
            for (Player player: team.getPlayers())
                if (player.hasWizard())
                    if (player.getWizard().getId() == wizardId)
                        return false;
        if(wizardId < 1 || wizardId > 5)
            return false;
        return true;
    }
}