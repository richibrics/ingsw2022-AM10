package it.polimi.ingsw.model.game_components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Table {
    final private ArrayList<SchoolBoard> schoolBoards;
    final private Bag bag;
    final private ArrayList<CloudTile> cloudTiles;
    final private MotherNature motherNature;
    final private ArrayList<ArrayList<IslandTile>> islandTiles;
    final private ArrayList<ProfessorPawn> availableProfessorPawns;
    final private Map<Integer, CharacterCard> activeCharacterCards;
    private int availableNoEntryTiles;

    public Table(ArrayList<SchoolBoard> schoolBoards, Bag bag, ArrayList<CloudTile> cloudTiles, MotherNature motherNature, ArrayList<ArrayList<IslandTile>> islandTiles, ArrayList<ProfessorPawn> availableProfessorPawns, Map<Integer, CharacterCard> activeCharacterCards) {
        this.schoolBoards = new ArrayList<>(schoolBoards);
        this.bag = bag;
        this.cloudTiles = new ArrayList<>(cloudTiles);
        this.motherNature = motherNature;
        // Copy the island groups received
        this.islandTiles = new ArrayList<>();
        for(ArrayList<IslandTile> group: islandTiles)
        {
            this.islandTiles.add(new ArrayList<>(group));
        }
        // End of island groups copy
        this.availableProfessorPawns = availableProfessorPawns;
        this.activeCharacterCards = new HashMap<>(activeCharacterCards);
        this.availableNoEntryTiles = 4;
    }


    /**
     * Returns the ArrayList of match SchoolBoards
     *
     * @return  ArrayList of SchoolBoards
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
     * Gets a copy of the list of available professor pawns.
     * @return the list of available professor pawns
     */

    public ArrayList<ProfessorPawn> getAvailableProfessorPawns() {
        return new ArrayList<>(this.availableProfessorPawns);
    }

    /**
     * Pops the requested professor pawn from the list of available professor pawns.
     * @param color the color of the requested professor pawn
     * @return the required professor pawn
     * @throws NoSuchElementException if the professor pawn could not be found
     */

    public ProfessorPawn popProfessorPawn (PawnColor color) throws NoSuchElementException{
        ProfessorPawn professorPawn = null;
        for (ProfessorPawn pawn : this.availableProfessorPawns)
            if (pawn.getColor() == color) {
                professorPawn = pawn;
                this.availableProfessorPawns.remove(pawn);
            }
        if (professorPawn == null)
            throw new NoSuchElementException("Professor pawn not found");
        return professorPawn;
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
     * Returns the number of NoEntry tiles available to be set to IslandTile groups.
     * @return the number of NoEntry tiles
     */
    public int getAvailableNoEntryTiles() { return this.availableNoEntryTiles; }

    /**
     * Increases the number of available NoEntry tiles by 1.
     */
    public void increaseAvailableNoEntryTiles() { this.availableNoEntryTiles += 1; }

    /**
     * Decreases the number of available NoEntry tiles by 1.
     */
    public void decreaseAvailableNoEntryTiles() { this.availableNoEntryTiles -= 1; }
}
