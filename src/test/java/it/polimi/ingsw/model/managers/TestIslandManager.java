package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.TowerAlreadySetException;
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
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null, matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
        // Check anything has changed
        assertEquals(4,table.getIslandTiles().size());
        // Now I set the first Island to White influence
        assertDoesNotThrow(()->table.getIslandTiles().get(0).get(0).setTower(new Tower(TowerColor.BLACK)));
        /*
         *
         * STATE:
         * island(0, BLACK)
         * island(1, free)
         * island(2, free)
         * island(3, free)
         *
         */
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
        // Check anything has changed
        assertEquals(4,table.getIslandTiles().size());

        // Set island with id 1 to Black, now 0 and 1 should unify
        assertDoesNotThrow(()->table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK)));
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
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
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

        // Now I set the island 3 to Black
        assertDoesNotThrow(()->table.getIslandTiles().get(2).get(0).setTower(new Tower(TowerColor.BLACK)));
        /*
         *
         * STATE:
         * island(2, free)
         * island(3, BLACK), island(0, BLACK), island(1, BLACK)
         *
         */
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
        // Check we have lost a group
        assertEquals(2,table.getIslandTiles().size());
        // Check group 1 now has 1 island tiles (and the other 3)
        assertEquals(1,table.getIslandTiles().get(0).size());
        assertEquals(3,table.getIslandTiles().get(1).size());


        // I finish with setting island 2 to Black and checking I have everything unified (Test multi-union)
        assertDoesNotThrow(()->table.getIslandTiles().get(0).get(0).setTower(new Tower(TowerColor.BLACK)));
        /*
         *
         * STATE:
         * island(3, BLACK), island(0, BLACK), island(1, BLACK), island(2, BLACK)
         *
         *
         */
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
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
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        table.getIslandTiles().get(0).get(0).setNoEntry(true);
        table.getIslandTiles().get(2).get(0).setNoEntry(true);
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
        // Should have 4 groups and NoEntry only on the 0,2 islands
        assertEquals(4, table.getIslandTiles().size());
        assertTrue(table.getIslandTiles().get(0).get(0).hasNoEntry());
        assertFalse(table.getIslandTiles().get(1).get(0).hasNoEntry());
        assertTrue(table.getIslandTiles().get(2).get(0).hasNoEntry());
        assertFalse(table.getIslandTiles().get(3).get(0).hasNoEntry());

        // Now I unify island 0,1 and 2,3 (every island should get NoEntry)
        assertDoesNotThrow(()->table.getIslandTiles().get(0).get(0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->table.getIslandTiles().get(2).get(0).setTower(new Tower(TowerColor.WHITE)));
        assertDoesNotThrow(()->table.getIslandTiles().get(3).get(0).setTower(new Tower(TowerColor.WHITE)));
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
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

        // Try to set towers where already present and assertThrow
        assertThrows(TowerAlreadySetException.class,()->table.getIslandTiles().get(1).get(0).setTower(new Tower(TowerColor.BLACK)));
        assertThrows(TowerAlreadySetException.class,()->table.getIslandTiles().get(1).get(1).setTower(new Tower(TowerColor.BLACK)));

        // Now do it in the correct way
        assertDoesNotThrow(()->table.getIslandTiles().get(1).get(0).replaceTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->table.getIslandTiles().get(1).get(1).replaceTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());

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
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),motherNature,matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertEquals(2,assertDoesNotThrow(()->gameEngine.getIslandManager().getMotherNatureIslandId()));
    }

    /**
     * Test islandTileHasNoEntry: set directly the property on the IslandTile and assert with islandHasNoEntry the value.
     */
    @Test
    void islandTileHasNoEntry() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(), new Bag(), new ArrayList<>(), null, matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertFalse(assertDoesNotThrow(() ->
                gameEngine.getIslandManager().islandTileHasNoEntry(0)));
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).setNoEntry(true));
        assertTrue(assertDoesNotThrow(() ->
                gameEngine.getIslandManager().islandTileHasNoEntry(0)));
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).setNoEntry(false));
        assertFalse(assertDoesNotThrow(() ->
                gameEngine.getIslandManager().islandTileHasNoEntry(0)));
    }

    /**
     * Test setIslandGroupNoEntryByIslandId: set IslandTile NoEntry status to both true and false and check the result is correct
     */
    @Test
    void setIslandGroupNoEntryByIslandId() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.get(0).add(new IslandTile(1));
        Table table = new Table(new ArrayList<>(), new Bag(), new ArrayList<>(), null, matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertDoesNotThrow(() -> gameEngine.getIslandManager().setIslandGroupNoEntryByIslandId(0, true));
        // check on the entire group
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).hasNoEntry()));
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));
        assertDoesNotThrow(() -> gameEngine.getIslandManager().setIslandGroupNoEntryByIslandId(0, false));
        // check on the entire group
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 0).hasNoEntry()));
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));
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
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertEquals(4, assertDoesNotThrow(()->gameEngine.getIslandManager().getIslandGroupsNumber()));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, 0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, 1).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getIslandManager().getIslandGroupsNumber()));
    }

    /**
     * Test getIslandTowerColor: test at first where there is not a Tower (assertThrows) then set a tower and check that it gets the new tower color.
     */
    @Test
    void getIslandTowerColor() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertThrows(TowerNotSetException.class, ()-> gameEngine.getIslandManager().getIslandTowerColor(0));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine,0).setTower(new Tower(TowerColor.BLACK)));
        assertDoesNotThrow(()-> gameEngine.getIslandManager().getIslandTowerColor(0));;
    }

    /**
     * Test getIslandTowerColor: test at first where there is not a Tower (assertFalse) then set a tower and assertTrue.
     */
    @Test
    void islandTileHasTower() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),null,matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        gameEngine.setTable(table);

        assertFalse(assertDoesNotThrow(()-> gameEngine.getIslandManager().islandTileHasTower(0)));
        assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine,0).setTower(new Tower(TowerColor.BLACK)));
        assertTrue(assertDoesNotThrow(()-> gameEngine.getIslandManager().islandTileHasTower(0)));
    }

    /**
     * Test getAvailableIslandTilesForMotherNature: check that I receive the correct islands and that if I have more
     * movements than island groups, I don't receive twice the id of the islands I can visit with Mother Nature.
     */
    @Test
    void getAvailableIslandTilesForMotherNature() {
        GameEngine gameEngine = setupForMotherNatureTests();

        // Pick card with 3 movements
        gameEngine.getTeams().get(0).getPlayers().get(0).setActiveAssistantCard(0);

        ArrayList<Integer> possibleIslands = assertDoesNotThrow(()->gameEngine.getIslandManager().getAvailableIslandTilesForMotherNature(1));

        /*
         * I check if I received what I want:
         * I should have 5, 0, 1 ,2 and not 3, 4
         */

        assertTrue(possibleIslands.contains(5));
        assertTrue(possibleIslands.contains(0));
        assertTrue(possibleIslands.contains(1));
        assertTrue(possibleIslands.contains(2));
        assertFalse(possibleIslands.contains(3));
        assertEquals(4, possibleIslands.size());

        // Now I change the card in hand with one that has 10 movements: I can test if I have all the islands and no duplicates
        gameEngine.getTeams().get(0).getPlayers().get(0).setActiveAssistantCard(1);

        // Recalculate
        possibleIslands = assertDoesNotThrow(()->gameEngine.getIslandManager().getAvailableIslandTilesForMotherNature(1));
        // Check
        assertTrue(possibleIslands.contains(5));
        assertTrue(possibleIslands.contains(0));
        assertTrue(possibleIslands.contains(1));
        assertTrue(possibleIslands.contains(2));
        assertTrue(possibleIslands.contains(3));
        assertEquals(6, possibleIslands.size());
    }

    /**
     * Test moveTowerFromPlayerSchoolBoardToIsland: move Tower from a player to an island without tower, here I check
     * that in the player I haven't that tower anymore and on the island I have the tower.
     * Additionally, try to move the island of another Team there and check also that the previous Tower is back on the
     * Team towers list.
     */
    @Test
    void moveTowerFromPlayerSchoolBoardToIsland() {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));

        ArrayList<Player> playersTeam1 = new ArrayList<>();
        playersTeam1.add(new Player(new User("a",0),1,1));

        ArrayList<Player> playersTeam2 = new ArrayList<>();
        playersTeam2.add(new Player(new User("b",0),2,1));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(new Team(1, playersTeam1));
        teams.add(new Team(2, playersTeam2));

        ArrayList<SchoolBoard> schoolboards = new ArrayList<>();
        schoolboards.add(new SchoolBoard());
        schoolboards.add(new SchoolBoard());
        playersTeam1.get(0).setSchoolBoard(schoolboards.get(0));
        playersTeam2.get(0).setSchoolBoard(schoolboards.get(1));

        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),new MotherNature(matrix.get(0).get(0)),matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(teams);
        gameEngine.setTable(table);

        Tower tower1 = new Tower(TowerColor.BLACK);
        teams.get(0).addTower(tower1);
        Tower tower2 = new Tower(TowerColor.WHITE);
        teams.get(1).addTower(tower2);

        // Set first tower (black) (no return needed)
        assertEquals(1,teams.get(0).getTowers().size());
        assertFalse(matrix.get(0).get(0).hasTower());
        assertDoesNotThrow(()->gameEngine.getIslandManager().moveTowerFromPlayerSchoolBoardToIsland(1,0));
        assertEquals(0,teams.get(0).getTowers().size()); // Pop ok
        assertTrue(matrix.get(0).get(0).hasTower());

        // Set there the White: return black to team 0
        assertEquals(0,teams.get(0).getTowers().size());
        assertEquals(1,teams.get(1).getTowers().size());
        assertDoesNotThrow(()->gameEngine.getIslandManager().moveTowerFromPlayerSchoolBoardToIsland(2,0));
        assertEquals(1,teams.get(0).getTowers().size()); // Returned
        assertEquals(0,teams.get(1).getTowers().size()); // Pop ok
        assertTrue(matrix.get(0).get(0).hasTower());
        // Check correct color set
        assertEquals(TowerColor.WHITE, assertDoesNotThrow(()->matrix.get(0).get(0).getTower().getColor()));
    }

    /**
     * Test moveMotherNature: check she doesn't move where she can't and that she moves where she can
     */
    @Test
    void moveMotherNature() {
        GameEngine gameEngine = setupForMotherNatureTests();

        // Pick card with 3 movements
        gameEngine.getTeams().get(0).getPlayers().get(0).setActiveAssistantCard(0);

        /*
         * Can go to 5, 0, 1, 2
         * Can't go to 3, 4
         * MM is on
         */

        // Go to far island: should get exception
        assertThrows(IllegalArgumentException.class,()-> gameEngine.getIslandManager().moveMotherNature(1, 4));
        // Not changed
        assertEquals(3,assertDoesNotThrow(()->gameEngine.getIslandManager().getMotherNatureIslandId()));
        // Now go to legal island
        assertDoesNotThrow(()-> gameEngine.getIslandManager().moveMotherNature(1, 0));
        // Check movement done
        assertEquals(0,assertDoesNotThrow(()->gameEngine.getIslandManager().getMotherNatureIslandId()));
    }

    /**
     * Setups the GameEngine to test moveMotherNature and getAvailableIslandTilesForMotherNature
     * @return
     */
    GameEngine setupForMotherNatureTests()
    {
        ArrayList<ArrayList<IslandTile>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add(new IslandTile(0));
        matrix.add(new ArrayList<>());
        matrix.get(1).add(new IslandTile(1));
        matrix.get(1).add(new IslandTile(2));
        matrix.add(new ArrayList<>());
        matrix.get(2).add(new IslandTile(3));
        matrix.get(2).add(new IslandTile(4));
        matrix.add(new ArrayList<>());
        matrix.get(3).add(new IslandTile(5));

        /*
         * Island state:
         * group 0: island 0
         * group 1: island 1, island 2
         * group 2: island 3, island 4 [MM on 3]
         * group 3: island 5
         */

        ArrayList<Player> playersTeam1 = new ArrayList<>();
        playersTeam1.add(new Player(new User("a",0),1,1));
        playersTeam1.add(new Player(new User("b",0),2,1));
        ArrayList<Player> playersTeam2 = new ArrayList<>();
        playersTeam2.add(new Player(new User("c",0),3,1));
        playersTeam2.add(new Player(new User("d",0),4,1));
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(new Team(0, playersTeam1));
        teams.add(new Team(1, playersTeam2));

        // 2 Teams: p1,p2 and p3,p4

        Table table = new Table(new ArrayList<>(),new Bag(),new ArrayList<>(),new MotherNature(matrix.get(2).get(0)),matrix, new ArrayList<>(), new HashMap<>());
        GameEngine gameEngine = new GameEngine(teams);
        gameEngine.setTable(table);

        // set only one card for testing purposes
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        assistantCards.add(new AssistantCard(0,2,3));
        assistantCards.add(new AssistantCard(1,2,10));
        playersTeam1.get(0).setWizard(new Wizard(0,assistantCards));

        return gameEngine;
    }
}