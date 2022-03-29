package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.MotherNature;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMotherNature {

    @Test
    public void testIslandTile()
    {
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
