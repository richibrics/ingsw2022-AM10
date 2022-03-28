package model.game_components;

import model.exceptions.TowerNotSetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;


public class TestIslandTile {
    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        IslandTile islandTile = new IslandTile(value);
        assertEquals(islandTile.getId(),value);
    }

    @Test
    public void testStudent()
    {
        IslandTile islandTile = new IslandTile(0);
        StudentDisc studentDisc = new StudentDisc(1,PawnColor.BLUE);
        StudentDisc studentDisc2 = new StudentDisc(2,PawnColor.BLUE);
        islandTile.addStudent(studentDisc);
        islandTile.addStudent(studentDisc2);
        assertEquals(islandTile.peekStudents().size(),2);
        assertEquals(islandTile.peekStudents().get(0),studentDisc);
        assertDoesNotThrow(()->islandTile.removeStudent(studentDisc));
        assertEquals(islandTile.peekStudents().size(),1);
        assertThrows(NoSuchElementException.class,()->islandTile.removeStudent(studentDisc));
    }

    @Test
    public void testTower()
    {
        IslandTile islandTile = new IslandTile(0);
        Tower blackTower = new Tower(TowerColor.BLACK);
        Tower whiteTower = new Tower(TowerColor.WHITE);
        assertThrows(TowerNotSetException.class,() -> islandTile.getTower());
        assertThrows(TowerNotSetException.class,() -> islandTile.replaceTower(blackTower));
        islandTile.setTower(blackTower);
        assertEquals(assertDoesNotThrow(()->islandTile.getTower()),blackTower);
        assertEquals(assertDoesNotThrow(()->islandTile.replaceTower(whiteTower)),blackTower);
        assertEquals(assertDoesNotThrow(()->islandTile.getTower()),whiteTower);
    }

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
