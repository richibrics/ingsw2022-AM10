package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientRound {
    private ArrayList<Integer> listOfActions;
    private int currentPlayer;

    public ClientRound(ArrayList<Integer> listOfActions, int currentPlayer) {
        this.listOfActions = listOfActions;
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Integer> getPossibleActions() {
        return this.listOfActions;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
}
