package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.client.ClientServerConnection;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;

import java.util.ArrayList;

public class GUI implements ViewInterface {
    @Override
    public void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams, int playerId) {
        
    }

    @Override
    public void displayLobby(ClientLobby clientLobby) {

    }

    @Override
    public void displayActions(ArrayList<Integer> possibleActions) {

    }

    @Override
    public void askForUser() {

    }

    @Override
    public void askToChangePreference() {

    }

    @Override
    public void setClientServerConnection(ClientServerConnection clientServerConnection) {

    }

    @Override
    public void displayWinners(String messageForPlayer) {

    }

    @Override
    public void showMenu(ClientTable clientTable, ClientTeams clientTeams, int playerId, ArrayList<Integer> possibleActions) {

    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public ActionMessage getActionMessage() {
        return null;
    }

    @Override
    public boolean userReady() {
        return false;
    }

    @Override
    public void setUserReady(boolean newValue) {

    }

    @Override
    public void showError(String message) {

    }
}
