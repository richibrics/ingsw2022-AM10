package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.Tower;
import it.polimi.ingsw.model.game_components.TowerColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTower {
    @ParameterizedTest
    @EnumSource(TowerColor.class)
    public void testGetColor(TowerColor color)
    {
        Tower tower = new Tower(color);
        assertEquals(tower.getColor(),color);
    }
}
