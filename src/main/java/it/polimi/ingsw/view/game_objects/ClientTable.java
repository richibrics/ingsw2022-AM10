package it.polimi.ingsw.view.game_objects;

import java.util.ArrayList;

public class ClientTable {

    private final ArrayList<ClientSchoolBoard> schoolBoards;
    private final ClientBag bag;
    private final ArrayList<ClientCloudTile> cloudTiles;
    private final ClientMotherNature motherNature;
    private final ArrayList<ArrayList<ClientIslandTile>> islandTiles;
    private final ArrayList<ClientPawnColor> availableProfessorPawns;
    private final ArrayList<ClientCharacterCard> activeCharacterCards;
    private final int availableNoEntryTiles;

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

    /**
     * Returns the schoolBoards of the client Table
     *
     * @return schoolBoards
     * @see ClientSchoolBoard
     */
    public ArrayList<ClientSchoolBoard> getSchoolBoards() {
        return this.schoolBoards;
    }

    /**
     * Returns the current state of the client bag
     *
     * @return Bag
     * @see ClientBag
     */
    public ClientBag getBag() {
        return this.bag;
    }

    /**
     * Return the CloudTiles of the client Table
     *
     * @return CloudTiles
     * @see ClientCloudTile
     */
    public ArrayList<ClientCloudTile> getCloudTiles() {
        return this.cloudTiles;
    }

    /**
     * Returns the MotherNature status
     *
     * @return MotherNature
     * @see ClientMotherNature
     */
    public ClientMotherNature getMotherNature() {
        return this.motherNature;
    }

    /**
     * Gets the islandTiles of client Table
     *
     * @return IslandTiles of client Table
     * @see ClientIslandTile
     */
    public ArrayList<ArrayList<ClientIslandTile>> getIslandTiles() {
        return this.islandTiles;
    }

    /**
     * Gets the availableProfessorPawns of client Table.
     *
     * @return the available professor pawns
     * @see ClientPawnColor
     */
    public ArrayList<ClientPawnColor> getAvailableProfessorPawns() {
        return this.availableProfessorPawns;
    }

    /**
     * Returns the activeCharacterCards of client Table
     *
     * @return activeCharacterCards on the client Table
     * @see ClientCharacterCard
     */
    public ArrayList<ClientCharacterCard> getActiveCharacterCards() {
        return this.activeCharacterCards;
    }

    /**
     * Returns the number of NoEntry tiles available to be set to IslandTile groups.
     *
     * @return the number of NoEntry tiles
     */
    public int getAvailableNoEntryTiles() {
        return this.availableNoEntryTiles;
    }
}