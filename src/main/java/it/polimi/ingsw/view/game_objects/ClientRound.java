package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientRound {
    private final ArrayList<Integer> listOfActions;
    private final int currentPlayer;

    public ClientRound(ArrayList<Integer> listOfActions, int currentPlayer) {
        this.listOfActions = listOfActions;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns the ids of the possible Actions the client (that currently has the turn) can perform.
     *
     * @return the ids of the possible Actions the client (that currently has the turn) can perform
     */
    public ArrayList<Integer> getPossibleActions() {
        return this.listOfActions;
    }

    /**
     * Returns the id of the player that has to play (which has the turn in this round).
     *
     * @return the id of the player that has to play (which has the turn in this round)
     */
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
}
