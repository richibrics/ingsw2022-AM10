package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.view.game_objects.*;

import java.util.ArrayList;

public class ClientTable {

    private ArrayList<ClientSchoolBoard> schoolBoards;
    private ClientBag bag;
    private ArrayList<ClientCloudTile> cloudTiles;
    private ClientMotherNature motherNature;
    private ArrayList<ArrayList<ClientIslandTile>> islandTiles;
    private ArrayList<ClientPawnColor> availableProfessorPawns;
    private ArrayList<ClientCharacterCard> activeCharacterCards;
    private int availableNoEntryTiles;

    public ClientTable(ArrayList<ClientSchoolBoard> schoolBoards, ClientBag bag, ArrayList<ClientCloudTile> cloudTiles,
                       ClientMotherNature motherNature, ArrayList<ArrayList<ClientIslandTile>> islandTiles,
                       ArrayList<ClientPawnColor> availableProfessorPawns, ArrayList<ClientCharacterCard> activeCharacterCards,
                       int availableNoEntryTiles) {
        this.schoolBoards = schoolBoards;
        this.bag = bag;
        this.cloudTiles = cloudTiles;
        this.motherNature = motherNature;
        this.islandTiles = islandTiles;
        this.availableProfessorPawns = availableProfessorPawns;
        this.activeCharacterCards = activeCharacterCards;
        this.availableNoEntryTiles = availableNoEntryTiles;
    }

    public ArrayList<ClientSchoolBoard> getSchoolBoards() {
        return this.schoolBoards;
    }

    public ClientBag getBag() {
        return this.bag;
    }

    public ArrayList<ClientCloudTile> getCloudTiles() {
        return this.cloudTiles;
    }

    public ClientMotherNature getMotherNature() {
        return this.motherNature;
    }

    public ArrayList<ArrayList<ClientIslandTile>> getIslandTiles() {
        return this.islandTiles;
    }

    public ArrayList<ClientPawnColor> getAvailableProfessorPawns() {
        return this.availableProfessorPawns;
    }

    public ArrayList<ClientCharacterCard> getActiveCharacterCards() {
        return this.activeCharacterCards;
    }

    public int getAvailableNoEntryTiles() {
        return this.availableNoEntryTiles;
    }
}