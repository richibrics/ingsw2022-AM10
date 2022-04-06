package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

class TestTable {

    static ArrayList<SchoolBoard> schoolBoards;
    static Bag bag;
    static ArrayList<CloudTile> cloudTiles;
    static MotherNature motherNature;
    static ArrayList<ArrayList<IslandTile>> islandTiles;
    static ArrayList<ProfessorPawn> professorPawns;
    static Map<Integer, CharacterCard> characterCards;

    @BeforeEach
    void beforeEach() {
        schoolBoards = new ArrayList<>();
        bag = new Bag();
        cloudTiles = new ArrayList<>();
        islandTiles = new ArrayList<>();
        professorPawns = new ArrayList<>();
        characterCards = new HashMap<>();
    }

    @Test
    void getSchoolBoards() {
        SchoolBoard schoolBoard = new SchoolBoard();
        schoolBoards.add(schoolBoard);
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(schoolBoards, table.getSchoolBoards());
    }

    @Test
    void getBag() {
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(bag, table.getBag());
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 1, 2, 3, 4})
    void getCloudTiles(int value) {
        CloudTile cloudTile = new CloudTile(value);
        cloudTiles.add(cloudTile);
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(cloudTiles, table.getCloudTiles());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, Integer.MAX_VALUE})
    void getMotherNature(int value) {
        IslandTile islandTile = new IslandTile(value);
        motherNature = new MotherNature(islandTile);
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(motherNature.getIslandTile(), table.getMotherNature().getIslandTile());
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 3, 6, 9})
    void getIslandTiles(int value) {
        IslandTile islandTile = new IslandTile(value);
        ArrayList<IslandTile> islandGroup = new ArrayList<IslandTile>();
        islandGroup.add(islandTile);
        islandTiles.add(islandGroup);
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(islandTiles, table.getIslandTiles());
    }

    @Test
    void getAvailableNoEntryTiles() {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        Table table = new Table(schoolBoards, null, cloudTiles, null, islandTiles, characterCards);
        assertEquals(4, table.getAvailableNoEntryTiles());
    }

    @Test
    void decreaseandincreaseAvailableNoEntryTiles() {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        Table table = new Table(schoolBoards, null, cloudTiles, null, islandTiles, characterCards);
        table.decreaseAvailableNoEntryTiles();
        assertEquals(3, table.getAvailableNoEntryTiles());
        table.decreaseAvailableNoEntryTiles();
        table.decreaseAvailableNoEntryTiles();
        assertEquals(1, table.getAvailableNoEntryTiles());
        table.increaseAvailableNoEntryTiles();
        table.increaseAvailableNoEntryTiles();
        table.increaseAvailableNoEntryTiles();
        assertEquals(4, table.getAvailableNoEntryTiles());
    }
      
    void getAvailableProfessorPawns() {
        for (PawnColor color : PawnColor.values()) {
            ProfessorPawn professorPawn = new ProfessorPawn(color);
            professorPawns.add(professorPawn);
        }
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        int index = 0;
        for (ProfessorPawn professorPawn : professorPawns) {
            assertEquals(professorPawn, table.getAvailableProfessorPawns().get(index));
            index++;
        }
    }

    @Test
    void popProfessorPawn() {
        for (PawnColor color : PawnColor.values()) {
            ProfessorPawn professorPawn = new ProfessorPawn(color);
            professorPawns.add(professorPawn);
        }
        Table table = new Table(schoolBoards, bag, cloudTiles, motherNature, islandTiles, professorPawns, characterCards);
        assertEquals(PawnColor.RED, assertDoesNotThrow(()->table.popProfessorPawn(PawnColor.RED).getColor()));
        assertEquals(table.getAvailableProfessorPawns().size(), 4);
        assertThrows(NoSuchElementException.class, ()->table.popProfessorPawn(PawnColor.RED));
    }

    @Test
    void getCharacterCard() {

    }
}
