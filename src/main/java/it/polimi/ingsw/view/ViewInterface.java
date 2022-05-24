package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.client.ClientServerConnection;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;

import java.util.ArrayList;

public interface ViewInterface {

    void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams);

    void displayLobby(ClientLobby clientLobby);

    void displayActions(ArrayList<Integer> possibleActions);

    void askForUser();

    void askToChangePreference();

    void setClientServerConnection(ClientServerConnection clientServerConnection);

    void displayWinners(String messageForPlayer);

    void showMenu(ClientTable clientTable, int playerId);

    User getUser();

    ActionMessage getActionMessage();

    boolean userReady();

    void setUserReady(boolean newValue);

    void showError(String message);
}
