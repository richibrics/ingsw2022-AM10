package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestIslandManager {

    /**
     * Test unifyPossibleIslands asking to unify when there's nothing to unify and when I also have to unify more
     * that a group at once.
     */
    @Test
    void unifyPossibleIslands() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.get(1).add(new IslandTile(1));
        matrix.get(2).add(new IslandTile(2));
        matrix.get(3).add(new IslandTile(3));
        /*
         *
         * STATE:
         * island(0, free)
         * island(1, free)
         * island(2, free)
         * island(3, free)
         *
         */
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Check anything has changed
        assertEquals(4,table.getIslandTiles().size());
        // Now I set the first Island to White influence
        table.getIslandTiles().get(0).get(0).setTower(new Tower(TowerColor.BLACK));
        /*
         *
         * STATE:
         * island(0, BLACK)
         * island(1, free)
         * island(2, free)
         * island(3, free)
         *
         */
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Check anything has changed
        assertEquals(4,table.getIslandTiles().size());

        // Set island with id 1 to Black, now 0 and 1 should unify
        table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK));
        /*
         *
         * STATE:
         * island(0, BLACK)
         * island(1, BLACK)
         * island(2, free)
         * island(3, free)
         *
         */
        // Unify
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Check we have lost a group
        assertEquals(3,table.getIslandTiles().size());
        // Check group 0 now has 2 island tiles (and the other still 1)
        assertEquals(2,table.getIslandTiles().get(0).size());
        assertEquals(1,table.getIslandTiles().get(1).size());
        assertEquals(1,table.getIslandTiles().get(2).size());
        /*
         *
         * STATE:
         * island(0, BLACK), island(1, BLACK)
         * island(2, free)
         * island(3, free)
         *
         */

        // Now I set the island 3 to Black, nothing should happened so I use the previous tests
        table.getIslandTiles().get(2).get(0).setTower(new Tower(TowerColor.BLACK));
        /*
         *
         * STATE:
         * island(0, BLACK), island(1, BLACK)
         * island(2, free)
         * island(3, BLACK)
         *
         */
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Check we have lost a group
        assertEquals(3,table.getIslandTiles().size());
        // Check group 0 now has 2 island tiles (and the other still 1)
        assertEquals(2,table.getIslandTiles().get(0).size());
        assertEquals(1,table.getIslandTiles().get(1).size());
        assertEquals(1,table.getIslandTiles().get(2).size());


        // I finish with setting island 2 to Black and checking I have everything unified (Test multi-union)
        table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK));
        /*
         *
         * STATE:
         * island(0, BLACK), island(1, BLACK)
         * island(2, BLACK)
         * island(3, BLACK)
         *
         */
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Check we have lost a group
        assertEquals(1,table.getIslandTiles().size());
        // Check group 0 now has 2 island tiles (and the other still 1)
        assertEquals(4,table.getIslandTiles().get(0).size());
        /*
         *
         * STATE:
         * island(0, BLACK), island(1, BLACK), island(2, BLACK), island(3, BLACK)
         *
         */
    }

    /**
     * Test a sub-feature of unifyPossibleIslands which is the ability to spread the NoEntry tiles to two IslandTiles groups
     * that are going to be merged
     */
    @Test
    public void unifyPossibleIslandsWithNoEntry()
    {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.get(1).add(new IslandTile(1));
        matrix.get(2).add(new IslandTile(2));
        matrix.get(3).add(new IslandTile(3));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        table.getIslandTiles().get(0).get(0).setNoEntry(true);
        table.getIslandTiles().get(2).get(0).setNoEntry(true);
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Should have 4 groups and NoEntry only on the 0,2 islands
        assertEquals(4, table.getIslandTiles().size());
        assertTrue(table.getIslandTiles().get(0).get(0).hasNoEntry());
        assertFalse(table.getIslandTiles().get(1).get(0).hasNoEntry());
        assertTrue(table.getIslandTiles().get(2).get(0).hasNoEntry());
        assertFalse(table.getIslandTiles().get(3).get(0).hasNoEntry());

        // Now I unify island 0,1 and 2,3 (every island should get NoEntry)
        table.getIslandTiles().get(0).get(0).setTower(new Tower(TowerColor.BLACK));
        table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK));
        table.getIslandTiles().get(2).get(0).setTower(new Tower(TowerColor.WHITE));
        table.getIslandTiles().get(3).get(0).setTower(new Tower(TowerColor.WHITE));
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        // Now I have 2 groups (0,1 black and 2,3 white)
        assertEquals(2, table.getIslandTiles().size());
        // Check everybody with NoEntry
        assertTrue(table.getIslandTiles().get(0).get(0).hasNoEntry());
        assertTrue(table.getIslandTiles().get(0).get(1).hasNoEntry());
        assertTrue(table.getIslandTiles().get(1).get(0).hasNoEntry());
        assertTrue(table.getIslandTiles().get(1).get(1).hasNoEntry());
        // Now I set every Island to White so they will unify: the 2 groups have the NoEntry tile
        // so the NoEntry tiles available have to increase
        assertEquals(4, table.getAvailableNoEntryTiles()); // Check current
        table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK));
        table.getIslandTiles().get(1).get(1).setTower(new Tower(TowerColor.BLACK));
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());

        // Final check: I should have only one group with all the islands which have the NoEntry tile.
        // The noEntry tiles available must become 4+1 = 5
        assertEquals(1, table.getIslandTiles().size());
        assertTrue(table.getIslandTiles().get(0).get(0).hasNoEntry());
        assertTrue(table.getIslandTiles().get(0).get(1).hasNoEntry());
        assertTrue(table.getIslandTiles().get(0).get(2).hasNoEntry());
        assertTrue(table.getIslandTiles().get(0).get(3).hasNoEntry());
        assertEquals(5, table.getAvailableNoEntryTiles()); // Check here NoEntry tile becomes available

    }

    @Test
    /**
     * Test getMotherNatureIslandId: check if the returned Island tile id is the same of the Island tile where MotherNature is
     */
    void getMotherNatureIslandId() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.get(1).add(new IslandTile(1));
        matrix.get(2).add(new IslandTile(2));
        matrix.get(3).add(new IslandTile(3));
        MotherNature motherNature = new MotherNature(matrix.get(2).get(0));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),motherNature,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertEquals(2,assertDoesNotThrow(()->islandManager.getMotherNatureIslandId()));
    }

    /**
     * Test islandTileHasNoEntry: set directly the property on the IslandTile and assert with islandHasNoEntry the value.
     */
    @Test
    void islandTileHasNoEntry() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(), new Bag(), new ArrayList<>(), null, matrix, new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertFalse(assertDoesNotThrow(() ->
                islandManager.islandTileHasNoEntry(0)));
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).setNoEntry(true));
        assertTrue(assertDoesNotThrow(() ->
                islandManager.islandTileHasNoEntry(0)));
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).setNoEntry(false));
        assertFalse(assertDoesNotThrow(() ->
                islandManager.islandTileHasNoEntry(0)));
    }

    /**
     * Test setIslandTileNoEntry: set IslandTile NoEntry status to both true and false and check the result is correct
     */
    @Test
    void setIslandTileNoEntry() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(), new Bag(), new ArrayList<>(), null, matrix, new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertDoesNotThrow(() -> islandManager.setIslandTileNoEntry(0, true));
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).hasNoEntry()));
        assertDoesNotThrow(() -> islandManager.setIslandTileNoEntry(0, false));
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).hasNoEntry()));
    }

    /**
     * Test getIslandGroupsNumber: insert 4 IslandTiles, set 2 of them with the same Tower color, unify, check I have only 3 groups now
     */
    @Test
    void getIslandGroupsNumber()
    {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.get(1).add(new IslandTile(1));
        matrix.get(2).add(new IslandTile(2));
        matrix.get(3).add(new IslandTile(3));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertEquals(4, assertDoesNotThrow(()->islandManager.getIslandGroupsNumber()));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, 0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, 1).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->islandManager.unifyPossibleIslands());
        assertEquals(3, assertDoesNotThrow(()->islandManager.getIslandGroupsNumber()));
    }

    /**
     * Test getIslandTowerColor: test at first where there is not a Tower (assertThrows) then set a tower and check that it gets the new tower color.
     */
    @Test
    void getIslandTowerColor() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertThrows(TowerNotSetException.class, ()-> islandManager.getIslandTowerColor(0));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine,0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()-> islandManager.getIslandTowerColor(0));;
    }

    /**
     * Test getIslandTowerColor: test at first where there is not a Tower (assertFalse) then set a tower and assertTrue.
     */
    @Test
    void islandTileHasTower() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix,new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);
        IslandManager islandManager = new IslandManager(gameEngine);

        assertFalse(assertDoesNotThrow(()-> islandManager.islandTileHasTower(0)));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine,0).setTower(new Tower(TowerColor.BLACK)));
        assertTrue(assertDoesNotThrow(()-> islandManager.islandTileHasTower(0)));
    }
}