package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientBag {

    /**
     * Creates a ClientBag that contains students.
     * Then it verifies if the number of students in the clientBag is correct.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {10, 100, 200})
    void getStudents(int value) {
        ClientBag clientBag = new ClientBag(value);
        assertEquals(value, clientBag.getStudents());
    }
}