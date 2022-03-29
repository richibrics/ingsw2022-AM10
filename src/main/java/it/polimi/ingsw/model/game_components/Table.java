package it.polimi.ingsw.model.game_components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    final private ArrayList<SchoolBoard> schoolBoards;
    final private Bag bag;
    final private ArrayList<CloudTile> cloudTiles;
    final private MotherNature motherNature;
    final private ArrayList<ArrayList<IslandTile>> islandTiles;
    final private Map<Integer, CharacterCard> activeCharacterCards;

    public Table(ArrayList<SchoolBoard> schoolBoards, Bag bag, ArrayList<CloudTile> cloudTiles, MotherNature motherNature, ArrayList<ArrayList<IslandTile>> islandTiles, Map<Integer, CharacterCard> activeCharacterCards) {
        this.schoolBoards = schoolBoards;
        this.bag = bag;
        this.cloudTiles = cloudTiles;
        this.motherNature = motherNature;
        this.islandTiles = islandTiles;
        this.activeCharacterCards = activeCharacterCards;
    }

    /**
     * Returns the ArrayList of match SchoolBoards
     *
     * @return  ArrayList of Schoolboards
     * @see     SchoolBoard
     */
    public ArrayList<SchoolBoard> getSchoolBoards() {
        return new ArrayList<>(this.schoolBoards);
    }

    /**
     * Returns the current state of the bag
     *
     * @return  Bag
     * @see     Bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Return the ArrayList of match Cloudtiles
     *
     * @return  ArrayList of CloudTiles
     * @see     CloudTile
     */
    public ArrayList<CloudTile> getCloudTiles() {
        return new ArrayList<>(this.cloudTiles);
    }

    /**
     * Returns the MotherNature status
     *
     * @return  MotherNature
     * @see     MotherNature
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Returns the matrix (ArrayList of ArrayList) of IslandTile in IslandTiles
     *
     * @return  Matrix of IslandTile in IslandTiles
     * @see     IslandTile
     */
    public ArrayList<ArrayList<IslandTile>> getIslandTiles() {
        return islandTiles;
    }

    /**
     * Returns the Table CharacterCards
     *
     * @return  The CharacterCards on the Table
     * @see     CharacterCard
     */
    public Map<Integer, CharacterCard> getCharacterCards() {
        return new HashMap<Integer, CharacterCard>(this.activeCharacterCards);
    }

    /**
     * Add a CharacterCard to the Table
     * @param  card CharacterCard to add to Table
     * @see     CharacterCard
     */
    public void addCharacterCard(CharacterCard card) {
        this.activeCharacterCards.put(card.getId(), card);
    }


}
