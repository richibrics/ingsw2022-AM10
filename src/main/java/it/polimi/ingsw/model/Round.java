package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.PlayerOrderNotSetException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class that manages the players turns order during the single match round, and for the only player who has currently
 * the turn, it stores the actions list that he can do.
 * Players' turns order is kept in a circular ArrayList to keep track of the initial order when the Round has ended.
 */
public class Round {
    final private ArrayList<Integer> possibleActions;
    final private ArrayList<Integer> orderOfPlay;
    final private int playersNumber;
    private int roundTurnsStatus;

    public Round(int playersNumber) {
        this.possibleActions = new ArrayList<>();
        this.orderOfPlay = new ArrayList<>();
        this.playersNumber = playersNumber;
        this.roundTurnsStatus = 0;
    }

    /**
     * Returns the ids of the possible Actions the Player (that currently has the turn) can perform.
     *
     * @return the ids of the possible Actions the Player (that currently has the turn) can perform
     */
    public ArrayList<Integer> getPossibleActions() {
        return new ArrayList<>(this.possibleActions);
    }

    /**
     * Sets a list of Actions ids that the current Player can perform.
     *
     * @param newListOfActions the list of Actions ids to set
     */
    public void setPossibleActions(ArrayList<Integer> newListOfActions) {
        this.possibleActions.clear();
        this.possibleActions.addAll(newListOfActions);
    }

    /**
     * Returns the ids of the Players ordered by their Turn order.
     *
     * @return the ids of the Players ordered by their Turn order
     */
    public ArrayList<Integer> getOrderOfPlay() {
        return new ArrayList<>(this.orderOfPlay);
    }

    /**
     * Sets the new Players turns order
     *
     * @param newOrder the list containing the ids of the Players in the new order
     * @throws InvalidParameterException if the new order size doesn't match players number or if a player is present more than once
     */
    public void setOrderOfPlay(ArrayList<Integer> newOrder) throws InvalidParameterException {
        if (newOrder.size() != this.playersNumber) {
            throw new InvalidParameterException("New order list size must be the same as number of players in the match");
        }
        if (new HashSet<Integer>(newOrder).size() < newOrder.size()) { // Check duplicates
            throw new InvalidParameterException("New order list cannot contain a player more than once");
        }
        this.roundTurnsStatus = 1;
        this.orderOfPlay.clear();
        this.orderOfPlay.addAll(newOrder);
    }

    /**
     * Returns the id of the player that has to play (which has the turn in this round).
     *
     * @return the id of the player that has to play (which has the turn in this round)
     * @throws PlayerOrderNotSetException if turns order is not set for this Round
     */
    public int getCurrentPlayer() throws PlayerOrderNotSetException {
        if (this.orderOfPlay.size() < 1)
            throw new PlayerOrderNotSetException();
        return orderOfPlay.get(0);
    }

    /**
     * Notify the observers that Round state has changed
     */
    public void notifyAllObservers() {
    } // Will be implemented after Observer implementation

    /**
     * This method is called from outside when the Player has ended its turn.
     * Increases the number of players that have played in this round (to know then when the Round ends).
     * Shifts the order of play to prepare the next player to be picked.
     * Check if all the players of the round have played; if yes, return false to let outside know the round has ended
     * and a new Player order is required.
     * If after this player, the round ended, the round restarts with the same order.
     *
     * @return false if after the current player there's nobody that has to play next
     */
    public boolean playerTurnEnded() {
        // At first set round status += 1
        this.roundTurnsStatus += 1;
        // Then shift the order play (if round has ended, it will become like at start)
        this.shiftOrderOfPlay();
        // Then return the status of round end
        return !this.hasRoundEnded();
    }

    /**
     * Shifts the order of play, moving first player if "orderOfPlay" list in the last position like if
     * it was a circular ArrayList.
     * The next player then will be ready to be picked in the first position.
     */
    private void shiftOrderOfPlay() {
        // Get currently first player and remove him from first position
        int lastPlayer = this.orderOfPlay.get(0);
        this.orderOfPlay.remove(Integer.valueOf(lastPlayer));
        // Insert him in the last position
        this.orderOfPlay.add(this.orderOfPlay.size(), lastPlayer);
    }

    /**
     * Returns true if all the players have played in this Round.
     *
     * @return true if all the players have played in this Round
     */
    private boolean hasRoundEnded() {
        if (this.roundTurnsStatus > this.playersNumber) {
            this.roundTurnsStatus = 1; // Restart the round with the same order
            return true;
        }
        return false;
    }
}
