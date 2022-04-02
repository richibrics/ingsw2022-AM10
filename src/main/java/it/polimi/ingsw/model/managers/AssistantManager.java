package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.Wizard;


import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AssistantManager extends Manager {

    public AssistantManager(GameEngine gameEngine) { super(gameEngine); }

    /**
     * Returns the player with {@code playerId} if possible, otherwise throws NoSuchElementException
     * @param playerId the identifier of the required player
     * @return the required player with {@code playerId}
     * @throws NoSuchElementException if the player could not be found
     * @see Player
     */

    private Player getPlayerById (int playerId) throws NoSuchElementException {
        Player playerTarget = null;
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                if (player.getPlayerId() == playerId)
                    playerTarget = player;

        if (playerTarget == null)
            throw  new NoSuchElementException("The player could not be found");

        return playerTarget;
    }

    /**
     * Creates all the assistant cards for the wizard with {@code wizardId}.
     * @param wizardId the identifier of the wizard
     * @return the assistant cards for the wizard with {@code wizardId}
     * @see AssistantCard
     * @see Wizard
     */

    private ArrayList<AssistantCard> createAssistantCards (int wizardId) {
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            assistantCards.add(new AssistantCard(wizardId == 1 ? wizardId * i : 10 * (wizardId - 1) + i, i, i % 2 == 0 ? i / 2 : i / 2 + 1));
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
        this.getPlayerById(playerId).setWizard(wizard);
    }

    /**
     * Sets the assistant card with {@code assistantId} as the current assistant card for the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @param assistantId the identifier of the assistant card
     * @throws NoSuchElementException if the player or the assistant card could not be found
     */

    public void setAssistantCard(int playerId, int assistantId) throws NoSuchElementException {
        this.getPlayerById(playerId).setActiveAssistantCard(assistantId);
    }

    /**
     * Gets the value of the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @return the value of the current assistant card of the player
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getValueOfAssistantCardInHand (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return this.getPlayerById(playerId).getActiveAssistantCard().getCardValue();
    }

    /**
     * Gets the movements of the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @return the number of movements of mother nature allowed by the card
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getMovementsOfAssistantCardInHand (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return this.getPlayerById(playerId).getActiveAssistantCard().getMovements();
    }

    /**
     * Gets the value of the assistant card played by the player with {@code playerId} in the previous round.
     * @param playerId the identifier of the player
     * @return the value of the assistant card played by the player with {@code playerId} in the previous round
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public int getValueOfLastPlayedAssistantCard (int playerId) throws NoSuchElementException, AssistantCardNotSetException {
        return  this.getPlayerById(playerId).getLastPlayedAssistantCard().getCardValue();
    }

    /**
     * Increments by {@code extraMovements} the movements allowed by the current assistant card of the player with {@code playerId}.
     * @param playerId the identifier of the player
     * @param extraMovements the extra movements allowed
     * @throws NoSuchElementException if the player could not be found
     * @throws AssistantCardNotSetException if the assistant card has not been set
     */

    public void incrementMovementsOfAssistantCardInHand (int playerId, int extraMovements) throws NoSuchElementException, AssistantCardNotSetException {
        this.getPlayerById(playerId).getActiveAssistantCard().incrementMovements(extraMovements);
    }

    /**
     * Pops the current assistant card of the player with {@code playerId} and makes that card the last played assistant card
     * of the same player.
     * @param playerId the identifier of the player
     * @throws NoSuchElementException if the player could not be found
     */

    public void moveAssistantCardInHandToLastPlayed (int playerId) throws NoSuchElementException {
        this.getPlayerById(playerId).setLastPlayedAssistantCard(this.getPlayerById(playerId).popActiveAssistantCard());
    }

    /*
    TODO
    Needs round to verify first player that chooses the assistant card (important in case two players can't play different assistant cards)
    public ArrayList<Integer> getPlayersPlayOrder () throws AssistantCardNotSetException {

    }
    */
}