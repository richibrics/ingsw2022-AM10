package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.exceptions.TowerAlreadySetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;


public class TestIslandTile {

    /**
     * Creates an IslandTiles and it checks if it returns the correct id
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        IslandTile islandTile = new IslandTile(value);
        assertEquals(islandTile.getId(),value);
    }

    /**
     * Creates an IslandTile and two different StudentDisc.
     * Then it adds the two StudentDisc to the IslandTile and checks if the IslandTile size is correctly 2.
     * At the end checks if the IslandTile peeks the correct StudentDisc.
     */
    @Test
    public void testStudent()
    {
        IslandTile islandTile = new IslandTile(0);
        StudentDisc studentDisc = new StudentDisc(1, PawnColor.BLUE);
        StudentDisc studentDisc2 = new StudentDisc(2, PawnColor.BLUE);
        islandTile.addStudent(studentDisc);
        islandTile.addStudent(studentDisc2);
        assertEquals(islandTile.peekStudents().size(),2);
        assertEquals(islandTile.peekStudents().get(0),studentDisc);
    }

    /**
     * Creates an IslandTile and two different Towers (one black and one white).
     * At the beginning it checks if the IslandTile is correctly empty and in this case it throws the TowerNotSetException.
     * Then it sets the Tower on the IslandTile and it verifies if it returns the correct Tower.
     * Next it checks if it is possible to replace the Tower with the other.
     * At the end it verifies if it returns the last Tower.
     */
    @Test
    public void testTower()
    {
        IslandTile islandTile = new IslandTile(0);
        Tower blackTower = new Tower(TowerColor.BLACK);
        Tower whiteTower = new Tower(TowerColor.WHITE);
        assertThrows(TowerNotSetException.class,() -> islandTile.getTower());
        assertFalse(islandTile.hasTower());
        assertThrows(TowerNotSetException.class,() -> islandTile.replaceTower(blackTower));
        assertFalse(islandTile.hasTower());
        assertDoesNotThrow(()->islandTile.setTower(blackTower));
        assertThrows(TowerAlreadySetException.class,()->islandTile.setTower(blackTower));
        assertTrue(islandTile.hasTower());
        assertEquals(assertDoesNotThrow(()->islandTile.getTower()),blackTower);
        assertEquals(assertDoesNotThrow(()->islandTile.replaceTower(whiteTower)),blackTower);
        assertEquals(assertDoesNotThrow(()->islandTile.getTower()),whiteTower);
    }

    /**
     * Creates an IslandTile.
     * Then checks if the IslandTile has a NoEntry property (before and after setting the NoEntry property).
     */
    @Test
    public void testNoEntry()
    {
        IslandTile islandTile = new IslandTile(1);
        assertFalse(islandTile.hasNoEntry());
        islandTile.setNoEntry(true);
        assertTrue(islandTile.hasNoEntry());
        islandTile.setNoEntry(false);
        assertFalse(islandTile.hasNoEntry());
    }
}
