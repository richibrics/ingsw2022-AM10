package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.model.game_components.TowerColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestClientIslandTile {

    /**
     * Creates a ClientIslandTile and it checks if it returns the correct parameters.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        ArrayList<Integer> students = new ArrayList<>();
        students.add(value);
        ClientIslandTile clientIslandTile = new ClientIslandTile(value + 1, students, true, TowerColor.BLACK);
        assertEquals(clientIslandTile.getId(), value + 1);
        assertEquals(clientIslandTile.getStudents(), students);
        assertTrue(clientIslandTile.hasNoEntry());
        assertEquals(clientIslandTile.getTower(), TowerColor.BLACK);
    }
}