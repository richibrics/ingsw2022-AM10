package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientCloudTile {

    /**
     * Creates a ClientCloudTile and checks if it returns the correct parameters.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        ArrayList<Integer> students = new ArrayList<>();
        students.add(value);
        ClientCloudTile clientCloudTile = new ClientCloudTile(value / 2, students);
        assertEquals(clientCloudTile.getId(), value / 2);
        assertEquals(clientCloudTile.getStudents(), students);
    }
}