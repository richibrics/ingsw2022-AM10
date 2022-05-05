package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.view.game_objects.ClientTeam;

import java.util.ArrayList;

public class ClientTeams {

    private ArrayList<ClientTeam> teams;

    public ClientTeams(ArrayList<ClientTeam> teams) {
        this.teams = teams;
    }

    public ArrayList<ClientTeam> getTeams() {
        return this.teams;
    }
}
