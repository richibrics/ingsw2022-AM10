package it.polimi.ingsw.view;

import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientRound;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;

public interface ViewInterface {

    void displayTable(ClientTable clientTable, ClientTeams clientTeams);

    void displayLobby(ClientLobby clientLobby);

    void displayAction(ClientRound clientRound);



}
