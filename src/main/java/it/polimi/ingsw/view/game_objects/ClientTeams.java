package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientTeams {

    private final ArrayList<ClientTeam> teams;

    public ClientTeams(ArrayList<ClientTeam> teams) {
        this.teams = teams;
    }

    /**
     * Gets the team in the client match.
     *
     * @return the teams
     * @see ClientTeam
     */
    public ArrayList<ClientTeam> getTeams() {
        return this.teams;
    }
}
