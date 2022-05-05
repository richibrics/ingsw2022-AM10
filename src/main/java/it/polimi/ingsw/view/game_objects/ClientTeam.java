package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientTeam {

    private ClientTowerColor towersColor;
    private int numberOfTowers;
    private ArrayList<ClientPawnColor> professorPawns;
    private ArrayList<ClientPlayer> players;

    public ClientTeam(ClientTowerColor towersColor, ArrayList<ClientPawnColor> professorPawns, int numberOfTowers, ArrayList<ClientPlayer> players) {
        this.towersColor = towersColor;
        this.professorPawns = professorPawns;
        this.numberOfTowers = numberOfTowers;
        this.players = players;
    }

    public ClientTowerColor getTowersColor() {
        return this.towersColor;
    }

    public ArrayList<ClientPawnColor> getProfessorPawns() {
        return this.professorPawns;
    }

    public int getNumberOfTowers() {
        return this.numberOfTowers;
    }

    public ArrayList<ClientPlayer> getPlayers() {
        return this.players;
    }
}
