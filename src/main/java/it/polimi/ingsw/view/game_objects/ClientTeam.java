package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientTeam {

    private final ClientTowerColor towersColor;
    private final int numberOfTowers;
    private final ArrayList<ClientPawnColor> professorPawns;
    private final ArrayList<ClientPlayer> players;

    public ClientTeam(ClientTowerColor towersColor, ArrayList<ClientPawnColor> professorPawns, int numberOfTowers, ArrayList<ClientPlayer> players) {
        this.towersColor = towersColor;
        this.professorPawns = professorPawns;
        this.numberOfTowers = numberOfTowers;
        this.players = players;
    }

    /**
     * Returns the TowerColor of the client Team.
     *
     * @return the TowerColor of the client Team
     * @see ClientTowerColor
     */
    public ClientTowerColor getTowersColor() {
        return this.towersColor;
    }

    /**
     * Gets the professorPawns of the client Team.
     *
     * @return the professorPawns of the client Team
     * @see ClientPawnColor
     */
    public ArrayList<ClientPawnColor> getProfessorPawns() {
        return this.professorPawns;
    }

    /**
     * Returns the numberOfTowers of the client Team.
     *
     * @return the numberOfTowers of the client Team
     */
    public int getNumberOfTowers() {
        return this.numberOfTowers;
    }

    /**
     * Gets the players in the client team.
     *
     * @return the players in the client team.
     * @see ClientPlayer
     */
    public ArrayList<ClientPlayer> getPlayers() {
        return this.players;
    }
}
