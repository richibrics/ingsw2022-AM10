package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestMotherNature {

    /**
     * Creates two different IslandTile and set MotherNature on the first one.
     * Then it checks if MotherNature is correctly on it.
     * Then MotherNature changes its IslandTile and goes to the second one.
     * At the end checks if MotherNature has changed correctly the IslandTile, switching the first with the second one.
     */
    @Test
    public void testIslandTile() {
        IslandTile startingIslandTile = new IslandTile(0);
        IslandTile nextIslandTile = new IslandTile(1);
        MotherNature motherNature = new MotherNature(startingIslandTile);
        assertEquals(motherNature.getIslandTile(), startingIslandTile);
        assertNotEquals(motherNature.getIslandTile(), nextIslandTile);
        motherNature.modifyIsland(nextIslandTile);
        assertNotEquals(motherNature.getIslandTile(), startingIslandTile);
        assertEquals(motherNature.getIslandTile(), nextIslandTile);
    }
}
