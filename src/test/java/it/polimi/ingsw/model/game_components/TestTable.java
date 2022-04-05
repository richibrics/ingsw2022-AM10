package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.MotherNature;
import it.polimi.ingsw.model.game_components.CloudTile;;
import it.polimi.ingsw.model.game_components.Table;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class TestTable {

    @Test
    void getSchoolBoards() {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();

        SchoolBoard schoolBoard = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        schoolBoards.add(schoolBoard);
        Table table = new Table(schoolBoards, null, cloudTiles, null, islandTiles, characterCards);
        assertEquals(schoolBoards, table.getSchoolBoards());
    }

    @Test
    void getBag() {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();

        Bag bag = new Bag();
        Table table = new Table(schoolBoards, bag, cloudTiles, null, islandTiles, characterCards);
        assertEquals(bag, table.getBag());
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 1, 2, 3, 4})
    void getCloudTiles(int value) {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();

        CloudTile cloudTile = new CloudTile(value);
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        cloudTiles.add(cloudTile);
        Table table = new Table(schoolBoards, null, cloudTiles , null, islandTiles, characterCards);
        assertEquals(cloudTiles, table.getCloudTiles());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, Integer.MAX_VALUE})
    void getMotherNature(int value) {
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();

        IslandTile islandTile = new IslandTile(value);
        MotherNature motherNature = new MotherNature(islandTile);
        Table table = new Table (schoolBoards, null, cloudTiles, new MotherNature(islandTile), islandTiles, characterCards);
        assertEquals(motherNature.getIslandTile(), islandTile);
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 3, 6, 9})
    void getIslandTiles(int value) {
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();

        IslandTile islandTile = new IslandTile(value);
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        ArrayList<IslandTile> islandTiles1 = new ArrayList<IslandTile>();
        islandTiles.add(islandTiles1);
        Table table = new Table(schoolBoards, null, cloudTiles, null, islandTiles, characterCards);
        assertEquals(islandTiles, table.getIslandTiles());
    }
}
