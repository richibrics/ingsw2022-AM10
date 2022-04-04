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
        SchoolBoard schoolBoard = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards= new ArrayList<>();
        schoolBoards.add(schoolBoard);
        Table table = new Table(schoolBoards, null, null, null, null, null);
        assertEquals(schoolBoards, table.getSchoolBoards());
    }

    @Test
    void getBag() {
        Bag bag = new Bag();
        Table table = new Table(null, bag, null, null, null, null);
        assertEquals(bag, table.getBag());
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 1, 2, 3, 4})
    void getCloudTiles(int value) {
        CloudTile cloudTile = new CloudTile(value);
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        cloudTiles.add(cloudTile);
        Table table = new Table(null, null, cloudTiles , null, null, null);
        assertEquals(cloudTiles, table.getCloudTiles());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, Integer.MAX_VALUE})
    void getMotherNature(int value) {
        IslandTile islandTile = new IslandTile(value);
        MotherNature motherNature = new MotherNature(islandTile);
        Table table = new Table (null, null, null, new MotherNature(islandTile), null, null);
        assertEquals(motherNature.getIslandTile(), islandTile);
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 3, 6, 9})
    void getIslandTiles(int value) {
        IslandTile islandTile = new IslandTile(value);
        ArrayList<ArrayList<IslandTile>> islandTiles= new ArrayList<ArrayList<IslandTile>>();
        ArrayList<IslandTile> islandTiles1 = new ArrayList<IslandTile>();
        islandTiles.add(islandTiles1);
        Table table = new Table(null, null, null, null, islandTiles, null);
        assertEquals(islandTiles, table.getIslandTiles());
    }
}
